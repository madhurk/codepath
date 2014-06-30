package madhur.codepath.tweetortweak;

import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;

public interface TweetViewListener {
  
  public void appendTweets(List<Tweet> tweets);
  public void replaceTweets(List<Tweet> tweets);
  public void handleRefreshComplete();
  public void prependTweets(List<Tweet> fetchedTweets);  
}
