package com.example.local.s3.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    public static final String BUCKET_NAME = "my-cool-bucket";

    @Bean
    @ConditionalOnProperty(name = "aws.s3.local", havingValue = "true", matchIfMissing = true)
    AmazonS3 local() {
        System.setProperty("aws.accessKeyId", "test");
        System.setProperty("aws.secretKey", "test");

        S3Mock s3Server = new S3Mock.Builder().withPort(8000).withInMemoryBackend().build();
        s3Server.start();

        var endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "eu-west");
        var creds = new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test"));
        var client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .withCredentials(creds)
                .build();

        client.createBucket(BUCKET_NAME);

        return client;
    }
}
