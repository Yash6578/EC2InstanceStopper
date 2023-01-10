package com.accenture.ec2instancestopper.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AwsController {

    @Autowired
    AWSCredentials awsCredentials;

    @GetMapping("aws/instance/stop")
    ResponseEntity stopsInstances() {
        List<InstanceStateChange> stoppedInstances = new ArrayList<>();
        // Create an Amazon EC2 client
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withCredentials(
                new AWSStaticCredentialsProvider(awsCredentials)
        ).withRegion("ap-south-1").build();

        // Initialize a DescribeInstancesRequest
        DescribeInstancesRequest request = new DescribeInstancesRequest();

        // Call the Amazon EC2 DescribeInstances method
        DescribeInstancesResult response = ec2.describeInstances(request);

        // Get the list of Reservations
        List<Reservation> reservations = response.getReservations();

        // Iterate over the Reservations list
        for (Reservation reservation : reservations) {
            // Get the list of Instances
            List<Instance> instances = reservation.getInstances();

            // Iterate over the Instances list
            for (Instance instance : instances) {
                // Print the Instance ID
                //System.out.println("Instance ID: " + instance.getInstanceId());

                // Stop the instance
                StopInstancesRequest stopRequest = new StopInstancesRequest()
                        .withInstanceIds(instance.getInstanceId());
                StopInstancesResult stopInstancesResult = ec2.stopInstances(stopRequest);
                String name = stopInstancesResult.getStoppingInstances().get(0).getCurrentState().getName();
                if(name.equals("stopping"))
                    stoppedInstances.add(stopInstancesResult.getStoppingInstances().get(0));

            }
        }

        return ResponseEntity.ok(stoppedInstances);
    }
}
