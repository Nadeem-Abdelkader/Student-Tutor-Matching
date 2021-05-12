package model;

import com.fasterxml.jackson.databind.JsonNode;
/**
 * This class models the Contract additional info, which contains the signing status of the two parties
 */
public class ContractAddInfo {
	
	private boolean firstPartySigned,
		secondPartySigned;
	
	public ContractAddInfo(boolean firstPartySigned, boolean secondPartySigned) {
		this.firstPartySigned = firstPartySigned;
		this.secondPartySigned = secondPartySigned;
	}
	
	public ContractAddInfo(JsonNode node) {
		this.firstPartySigned = Boolean.parseBoolean(node.get("firstPartySigned").textValue());
		this.secondPartySigned = Boolean.parseBoolean(node.get("secondPartySigned").textValue());
	}
	
	public String toJson() {
		return "{\"firstPartySigned\":\"" + this.firstPartySigned + "\"," +
				"\"secondPartySigned\":\"" + this.secondPartySigned + "\"}" ;
	}
	
	public String toString() {
		return "First party: " + (this.firstPartySigned? "signed":"") +
				"Second party: " + (this.secondPartySigned? "signed":"") ;
	}
	

	public boolean isFirstPartySigned() {
		return firstPartySigned;
	}
	public void firstPartySign(boolean firstPartySigned) {
		this.firstPartySigned = firstPartySigned;
	}
	public boolean isSecondPartySigned() {
		return secondPartySigned;
	}
	public void secondPartySign(boolean secondPartySigned) {
		this.secondPartySigned = secondPartySigned;
	}
	
	
}
