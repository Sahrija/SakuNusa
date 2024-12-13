package com.example.sakunusa.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class AnomalyResponse{

	@SerializedName("Anomaly Detected")
	private boolean anomalyDetected;

	@SerializedName("Amount")
	private Object amount;

	@SerializedName("Loss")
	private Object loss;

	@SerializedName("Date")
	private String date;

	public boolean isAnomalyDetected(){
		return anomalyDetected;
	}

	public Object getAmount(){
		return amount;
	}

	public Object getLoss(){
		return loss;
	}

	public String getDate(){
		return date;
	}
}