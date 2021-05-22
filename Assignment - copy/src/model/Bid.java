package model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mainview.Application;
import mainview.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class models a Bid
 */
public class Bid extends Observable implements Model{
    private String id;
    private BidType type;
    private User initiator;
    private Date dateCreated;
    private Subject subject;
	private boolean closeddown;
	private BidAddInfo addInfo;
	public static final int WEEK_IN_MILLIS = 3600 * 24 * 7 * 1000;
	public static final int HALF_HOUR_IN_MILLIS = 1800 * 1000;
	public static final int MIN_IN_MILLIS = 120 * 1000;
	public static final int COMPETENCY_PADDING = 2;
	
	public enum BidType {
		open,
		close;
	}
    
    /**
	 * Class representing a bid
	 * @param 
	 */
    public Bid (String id, BidType type, User initiator, Date dateCreated, Date dateClosedDown, Subject subject) {
        this.id = id;
        this.type = type;
        this.initiator = initiator;
        this.dateCreated = dateCreated;
        this.subject = subject;
    }
    public Bid() {}
    
    public Bid(JsonNode node) {
    	this.id = node.get("id").textValue();
    	this.type = BidType.valueOf(node.get("type").textValue());
    	this.dateCreated = Utils.formatDate(node.get("dateCreated").textValue());
    	if (node.get("dateClosedDown").textValue() == null) {
    		this.closeddown = false;
    	} else {
    		this.closeddown = true;
    	}
    	if (!(node.get("initiator") == null)) 
    		this.initiator = new User(node.get("initiator"));
    	else
    		this.initiator = null;
    	this.subject = new Subject(node.get("subject"));
    	if (node.get("additionalInfo").isEmpty())
    		this.addInfo = new BidAddInfo(null, null,null,null,null);
    	else
    		this.addInfo = new BidAddInfo(node.get("additionalInfo"));
    }

	public Bid(JsonNode node, User user) {
    	this.id = node.get("id").textValue();
    	this.type = BidType.valueOf(node.get("type").textValue());
    	this.dateCreated = Utils.formatDate(node.get("dateCreated").textValue());
    	if (node.get("dateClosedDown").textValue() == null) {
    		this.closeddown = false;
    	} else {
    		this.closeddown = true;
    	}
    	this.initiator = user;
    	this.subject = new Subject(node.get("subject"));
    	if (node.get("additionalInfo").isEmpty())
    		this.addInfo = new BidAddInfo(null, null,null,null,null);
    	else
    		this.addInfo = new BidAddInfo(node.get("additionalInfo"));
    }
    

    public String getId() {
        return id;
    }

    public BidType getType() {
        return type;
    }

    public String getInitiatorId() {
    	assert (this.initiator != null); //can't be called in student view when bid's initiator is null
    	return this.initiator.getId();
    }
    
    public String getDateCreated() {
        return dateCreated.toString();
    }

    public Subject getSubject() {
        return subject;
    }
    
    public String getInitiatorName() {
    	return initiator.getGivenName() + " " + initiator.getFamilyName();
    }
    
    @Override
    public String toString() {
        return (this.initiator == null ? 
        		"Type: " + this.type.toString() + '\n' +
				"Subject: " + this.getSubject() + '\n' +
				"Date created: " + this.getDateCreated() + '\n'
				+ this.addInfo.toString() + '\n':
				"Type: " + this.type.toString() + '\n' +
				"Initiator: " + this.getInitiatorName() +  '\n' +
				"Subject: " + this.getSubject() + '\n' +
				"Date created: " + this.getDateCreated() + '\n'
				+ this.addInfo.toString() + '\n');
    }
    
    private boolean isExpired() {
    	if (this.type.toString().equals("open") 
    			&& this.dateCreated.before(new Date(System.currentTimeMillis() - HALF_HOUR_IN_MILLIS))) {
    		return true;
    	} else if (this.type.toString().equals("close") 
    			&& this.dateCreated.before(new Date(System.currentTimeMillis() - WEEK_IN_MILLIS)))
    		return true;
    	return false;
    }
    
    public static List<Bid> getAll() {
		List<Bid> bids = new ArrayList<Bid> ();
		for (ObjectNode node : Model.getAll("/bid")) {
			bids.add(new Bid(node));
		}
		return screenClosedBid(bids);
	}
    
	public static List<Bid> screenClosedBid(List<Bid> bids) {
		List<Bid> screenedBids = new ArrayList<Bid>();

		for (Bid b : bids) {
			if ((!b.closeddown) &&  b.isExpired() ) {
				b.closeddown = true;
				if (b.addInfo != null && b.type == Bid.BidType.open
					&& !b.getResponse().isEmpty()) {
					BidResponse winner = b.getResponse().get(0);
					Contract.postContract(b.initiator.getId(), 
								winner.getBidderId(), 
								b.getSubject().getId(),
								new ContractAddInfo(false, false));
				}
				Bid.closeDownBid(b.id);
			} else if (!b.closeddown)
				screenedBids.add(b);
		}
		return screenedBids;
	}

    public void postBid(String type, String initiatorId, String subjectId, BidAddInfo addInfo) {
    	String url = Application.rootUrl + "/bid";
    	String jsonString = "{" +
  		"\"type\":\"" + type + "\"," +
  		"\"initiatorId\":\"" + initiatorId + "\"," +
  		"\"dateCreated\":\"" + Utils.format.format(new Date()) + "\"," +
  		"\"subjectId\":\"" + subjectId + "\"," +
  		"\"additionalInfo\":" + addInfo.toJson() + "}";
      
    	Model.post(url,  jsonString);
		this.inform();
    } 
    
    public void patchBid() {
    	String url = Application.rootUrl + "/bid/" + this.id;
    	
    	String jsonString = "{" + "\"additionalInfo\":" + this.addInfo.toJson() + "}";
    	
    	Model.patch(url, jsonString);
    }
    
    public static Bid updateBid(String bidId) {
    	
    	ObjectNode node = Model.get("/bid/", bidId);
    	return new Bid(node);
    
    }
    
    public static void updateBid(Bid bid) {
    	bid = new Bid(Model.get("/bid/", bid.id));
    }
    
    public static void closeDownBid(String bidId) {
    	String url = Application.rootUrl + "/bid/" + bidId + "/close-down";
    	String jsonString = "{" +
    	  		"\"dateClosedDown\":\"" + Utils.format.format(new Date()) + "\"}";
    	
    	Model.post(url, jsonString);
    	
    }   
    
	public List<Message> getMessages() {
		List<Message> messages = new ArrayList<Message>(); 
		ObjectNode node = Model.get("/bid/", this.id + "?fields=messages");
		assert (node.get("messages").isArray());
		Iterator<JsonNode> iter = node.get("messages").iterator();
		while (iter.hasNext()) {
			messages.add(new Message(iter.next(),this.id));
		}
		return messages;
	}
	
	public List<BidResponse> getResponse() {
		assert (this.addInfo != null);
		return this.addInfo.getResponses();
	}
	
	public void addResponse(BidResponse r) {
		this.addInfo.addResponse(r);
		this.patchBid();
	}
	
	public boolean checkEligibility(User tutor) {
		if (tutor.getCompetency(this.subject.getId()) >= this.addInfo.getPreferredCompetency() + COMPETENCY_PADDING) {

			return true;
		} else {
			System.out.println("preferred " + this.addInfo.getPreferredCompetency());
			return false;
		}
	}
}



