package com.geekday.accounting

import akka.actor.Actor

class AccountActor extends Actor {
  override def receive = {
    case _ ⇒ println("Receiving messages")
  }
}
