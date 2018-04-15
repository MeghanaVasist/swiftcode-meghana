package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

public class MessageActor extends UntypedActor {

    //Define another Actor
    private final ActorRef out;

    //Self reference the Actor
    public MessageActor(ActorRef out) {
        this.out = out;
    }

    //Define Props
    public static Props props(ActorRef out) {
        return Props.create(MessageActor.class, out);
    }

    //Object of FeedService and NewsAgentService
    private FeedService feedService = new FeedService();
    private NewsAgentService newsAgentService = new NewsAgentService();
    //private NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
    private FeedResponse feedResponse = new FeedResponse();

    @Override
    public void onReceive(Object message) throws Throwable {
        //Send back the response
        ObjectMapper mapper = new ObjectMapper();
        if (message instanceof String) {
            Message messageObject = new Message();
            messageObject.text = (String)message;
            messageObject.sender = Message.Sender.USER;
            //convert to JSON data to send to client
            out.tell(mapper.writeValueAsString(messageObject), self());
            String query = newsAgentService.getNewsAgentResponse("Find "+message,UUID.randomUUID()).query;
            feedResponse = feedService.getFeedByQuery(query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + query;
            messageObject.feedResponse = feedResponse;
            messageObject.sender = Message.Sender.BOT;
            out.tell(mapper.writeValueAsString(messageObject), self());
        }
    }
}