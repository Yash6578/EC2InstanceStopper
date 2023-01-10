package com.accenture.ec2instancestopper.config.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsCredentials {

    @Value("${cloud.aws.credentials.access-key}")
    String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    String secretKey;

    @Bean
    AWSCredentials getAwsCredentials() {
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }
}
