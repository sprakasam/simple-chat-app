package com.simple_chat.aws_queue

import com.simple_chat.main.{FizzMsgConsumer, BuzzMsgConsumer}
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder

import scala.util.control.NonFatal

class MySQSRouteBuilder extends RouteBuilder {

  override def configure() {
    try {
      from("aws-sqs://553482361591/SimpleChat?amazonSQSClient=#amazonSQSClient").process(new Processor() {
        def process(exchange: Exchange) {

          val msg = exchange.getIn().toString()
          if(msg.contains("fizz")) {
            BuzzMsgConsumer ! msg
          } else {
            FizzMsgConsumer ! msg
          }
        }
      })

    } catch {
      case NonFatal(e) => e.printStackTrace()
    }
  }

}