package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mainview.Application;
import mainview.Utils;

/**
 * Class that models a Contract
 */
public class Contract extends Observable implements Model {
	private String id;
	private User firstParty, secondParty;
	private Subject subject;
	private Date dateCreated, dateSigned, expiryDate, terminationDate;
	
	private ContractAddInfo addInfo;
	private static final int YEAR_IN_MILLIS = 3600 * 24 * 365 * 1000;
	
	public Contract(JsonNode node) {
		this.id = node.get("id").textValue();
		this.firstParty = new User(node.get("firstParty"));
		this.secondParty = new User(node.get("secondParty"));
		this.subject = new Subject(node.get("subject"));
		this.dateCreated = Utils.formatDate(node.get("dateCreated").textValue());
		this.dateSigned = Utils.formatDate(node.get("dateSigned").textValue());
		this.expiryDate = Utils.formatDate(node.get("expiryDate").textValue());
		
		this.addInfo = (node.get("additionalInfo").isEmpty()? null : new ContractAddInfo(node.get("additionalInfo")));
	}
	
	public static void postContract(String firstPartyId,
			String secondPartyId,
			String subjectId,
			ContractAddInfo addInfo) {
		String url = Application.rootUrl + "/contract";
		String jsonString = "{" +
		  		"\"firstPartyId\":\"" + firstPartyId + "\"," +
		  		"\"secondPartyId\":\"" + secondPartyId + "\"," +
				"\"subjectId\":\"" + subjectId + "\"," +
		  		"\"dateCreated\":\"" + Utils.format.format(new Date()) + "\"," +
		  		"\"expiryDate\":\"" + Utils.format.format(new Date(System.currentTimeMillis() + YEAR_IN_MILLIS)) + "\"," +
		  		"\"paymentInfo\":{}," +
		  		"\"lessonInfo\":{}," +
		  		"\"additionalInfo\":" + addInfo.toJson() + "}";
		    	
		Model.post(url, jsonString);
	}
	

	public void signContract() {
		this.addInfo.firstPartySign(true);
		this.addInfo.secondPartySign(true);
		
		String url = Application.rootUrl + "/contract/" + this.id + "/sign";
		String jsonString = "{" +
    	  		"\"dateSigned\":\"" + Utils.format.format(new Date()) + "\"}";
		Model.post(url, jsonString);
	}
	
	public static List<Contract> getAllContractsAsFirstParty(String userId) {
		List<Contract> allContracts = new ArrayList<Contract>();
		for (ObjectNode node : Model.getAll("/contract")) {
			Contract c = new Contract(node);
				
			if (c.firstParty.getId().equals(userId)) {
				c.updateDateSigned();
				allContracts.add(c);
			}
		}
		return allContracts;
	}
	
	public static List<Contract> getAllContractsAsSecondParty(String userId) {
		List<Contract> allContracts = new ArrayList<Contract>();
		for (ObjectNode node : Model.getAll("/contract")) {
			Contract c = new Contract(node);
			if (c.secondParty.getId().equals(userId))  {
				c.updateDateSigned();
				allContracts.add(c);
			}
		}
		return allContracts;
	}
	
	private void updateDateSigned() {
		if (this.addInfo == null)
			return;
		if (this.isSigned() && this.dateSigned == null) {
			this.signContract();
		} 
	}
	
	public String toString() {
		return "First Party: " + firstParty.getFullName() + '\n'
				+  "Second Party: " + secondParty.getFullName() + '\n'
				+  "Subject: " + '\n' + subject + '\n'
				+ "Date created: " + dateCreated + '\n'
				+ "Date signed: " + (dateSigned == null?"unsigned" : dateSigned) + '\n'
				+ "Date expire: " + expiryDate;
	}
	
	public boolean isSigned() {
		return firstPartySigned() && secondPartySigned(); 
	}
	
	public boolean firstPartySigned() {
		return this.addInfo.isFirstPartySigned();
	}
	
	public boolean secondPartySigned() {
		return this.addInfo.isSecondPartySigned();
	}

	public void firstPartySign(boolean sign) {
		this.addInfo.firstPartySign(sign);
		if (this.isSigned())
			this.signContract();
		else
			this.patchContract();
	}

	public void secondPartySign(boolean sign) {
		this.addInfo.secondPartySign(sign);
		if (this.isSigned())
			this.signContract();
		else
			this.patchContract();
	}

	public void patchContract() {
		String url = Application.rootUrl + "/contract/" + this.id;
		String jsonString = "{" +
		  		"\"firstPartyId\":\"" + firstParty.getId() + "\"," +
		  		"\"secondPartyId\":\"" + secondParty.getId() + "\"," +
				"\"subjectId\":\"" + subject.getId() + "\"," +
		  		"\"dateCreated\":\"" + Utils.format.format(new Date()) + "\"," +
		  		"\"expiryDate\":\"" + Utils.format.format(new Date(System.currentTimeMillis() + YEAR_IN_MILLIS)) + "\"," +
		  		"\"paymentInfo\":{}," +
		  		"\"lessonInfo\":{}," +
		  		"\"additionalInfo\":" + addInfo.toJson() + "}";
		    	
		Model.patch(url, jsonString);
	}
	
}
