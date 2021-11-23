package com.example.local.s3.demo;

import com.amazonaws.services.s3.AmazonS3;
import com.example.local.s3.demo.config.S3Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest(classes = {LocalS3Application.class})
class LocalS3ApplicationTests {
    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldUploadToLocalS3() throws IOException {
        var key = "my-dir/hello_world.txt";
        var originalContent = "Hello world";
        amazonS3.putObject(S3Config.BUCKET_NAME, key, originalContent);

        var contentFromS3 = new String(amazonS3.getObject(S3Config.BUCKET_NAME, key).getObjectContent().readAllBytes());

        log.info("Content from S3: {}", contentFromS3);
        assertThat(contentFromS3).isEqualTo(originalContent);
    }
}
