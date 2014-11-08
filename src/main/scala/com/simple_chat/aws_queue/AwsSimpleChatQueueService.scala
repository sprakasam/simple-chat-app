package com.simple_chat.aws_queue

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model._
import org.apache.camel.impl.{DefaultCamelContext, SimpleRegistry}
import scala.collection.JavaConversions._

import scala.util.control.NonFatal

object AwsSimpleChatQueueService {

  val (sqsClient, queueUrl) = {
    // TODO: read the credentials from system properties
    val credentials = new BasicAWSCredentials("AKIAIEWJXGPYQXUL2MHA", "DrS3LwwzoVkkm1AHjicOrQYwtyLM8QGmuvibVDau")
    val sqsClient = new AmazonSQSClient(credentials)
    sqsClient.setEndpoint("http://sqs.us-west-2.amazonaws.com")

    val getQueueUrlResult = sqsClient.getQueueUrl(new GetQueueUrlRequest("SimpleChat"))

    (sqsClient, getQueueUrlResult.getQueueUrl())
  }

//  def createCamelContext() = try {
//    // create CamelContext
//    val registry = new SimpleRegistry()
//    registry.put("amazonSQSClient" , sqsClient)
//    val context = new DefaultCamelContext(registry)
//
//    // add our route to the CamelContext
//    context.addRoutes(new MySQSRouteBuilder())
//
//    context.start()
//
//  } catch {
//    case NonFatal(e) => e.printStackTrace()
//  }

//  createCamelContext()

  def sendMsg(msg: String) =  {
    val result: SendMessageResult = sqsClient.sendMessage(new SendMessageRequest(queueUrl, msg))
  }

  def readMsg(msgType: String, idsToFilter: List[String]): List[MyMessage] = {
    val receiveMessageReq = new ReceiveMessageRequest(queueUrl)
    receiveMessageReq.setVisibilityTimeout(0)

    val messages: List[Message] = sqsClient.receiveMessage(receiveMessageReq).getMessages.toList
    val filteredMsgs = messages.filterNot(m => idsToFilter.contains(m.getMessageId))
    val tranformMsgs = filteredMsgs.map(m => MyMessage(m.getMessageId, m.getBody))
    tranformMsgs.filter(_.msg.contains(msgType))
  }

}

case class MyMessage(id: String, msg: String)
