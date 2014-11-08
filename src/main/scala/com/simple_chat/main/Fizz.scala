package com.simple_chat.main

import com.simple_chat.aws_queue.AwsSimpleChatQueueService

import scala.actors.Actor

// receives the message from apache camel and print it in console
object FizzMsgConsumer extends Actor {

  def act() {
    val messageIds = new scala.collection.mutable.ListBuffer[String]
    while (true) {
      val msgs = AwsSimpleChatQueueService.readMsg("buzz", messageIds.toList)
      if (!msgs.isEmpty) {
        msgs.foreach(m => messageIds += m.id)
        println(msgs.head.msg)
      }
      Thread.sleep(1000)
      //      receive {
      //        case msg => println(msg)
      //      }
    }
  }

}

// reads the input from console and sends it to amazon sqs queue
object FizzMsgSender extends Actor {
  def act() {
    while(true) {
      Iterator.continually(Console.readLine).takeWhile(_.nonEmpty).foreach { line =>
        AwsSimpleChatQueueService.sendMsg("fizz:" + line)
      }
    }
  }

}

object Fizz extends App {

  // initialize the amazon SQS simple queue and configure the camel route
  AwsSimpleChatQueueService

  FizzMsgConsumer.start()
  FizzMsgSender.start()
}
