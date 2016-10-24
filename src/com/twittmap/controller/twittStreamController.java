package com.twittmap.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twittmap.services.streamService;
import com.twittmap.services.twittService;

@Controller
public class twittStreamController {
	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/keyword/{keyWord}", method = RequestMethod.GET)
	public String streamKeyWord(@PathVariable String keyWord) {
		JSONObject json = new JSONObject();
		json.put("res", true);
		try {
			streamService.cleanUp(true);
			streamService.startStream(keyWord);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("res", false);
		}
		return json.toString();
	}

	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/queryTweets", method = RequestMethod.GET)
	public String queryTweets() {
		JSONArray tweets = new JSONArray();
		try {
			tweets = twittService.queryTweetsFromDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tweets.toString();
	}

	@ResponseBody
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/stopQuerying", method = RequestMethod.PUT)
	public String stopQuerying() {
		JSONObject json = new JSONObject();
		json.put("res", true);
		try {
			streamService.cleanUp(false);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("res", false);
		}
		return json.toString();
	}
}
