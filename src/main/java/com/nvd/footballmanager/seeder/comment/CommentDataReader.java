package com.nvd.footballmanager.seeder.comment;

import com.github.javafaker.Faker;
import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.model.entity.Feed;
import com.nvd.footballmanager.repository.FeedRepository;
import com.nvd.footballmanager.seeder.GenericDataReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class CommentDataReader implements GenericDataReader<Comment> {

    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
    private final TaskExecutor taskExecutor;

    private final FeedRepository feedRepository;


    public CommentDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
                             FeedRepository feedRepository) {
        this.taskExecutor = taskExecutor;
        this.feedRepository = feedRepository;
    }

    @Override
    public List<Comment> generateDataBatch(int batchSize) {
        List<Future<Comment>> futures = new ArrayList<>();
        List<Feed> feeds = feedRepository.findRandomLimit(batchSize);

        for (int i = 0; i < batchSize; i++) {
            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(feeds)));
        }

        List<Comment> comments = new ArrayList<>(batchSize);
        for (Future<Comment> future : futures) {
            try {
                comments.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        }
        return comments;
    }

    public Comment generateData(List<Feed> feeds) {
        Faker faker = fakerThreadLocal.get();
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setContent(faker.lorem().sentence());

        Feed feed = feeds.get(faker.random().nextInt(feeds.size()));
        comment.setFeed(feed);

//        String userId = userIds.get(faker.random().nextInt(userIds.size()));
//        User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
//        comment.setUser(user);

        comment.setCreatedAt(faker.date().past(200, TimeUnit.DAYS).toInstant());
        comment.setUpdatedAt(faker.date().between(Date.from(comment.getCreatedAt()),
                Date.from(comment.getCreatedAt().plus(3, ChronoUnit.DAYS))).toInstant());

        return comment;
    }

    @Override
    public Comment generateData() {
        return null;
    }
}
