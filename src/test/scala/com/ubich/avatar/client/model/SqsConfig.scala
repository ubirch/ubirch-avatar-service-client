package com.ubich.avatar.client.model

case class SqsConfig(queue: String,
                     region: String,
                     queueOwnerId: String,
                     accessKey: String,
                     secretAccessKey: String,
                     concurrentConsumers: Int = 2,
                     maxMessagesPerPoll: Option[Int] = None
                    )
