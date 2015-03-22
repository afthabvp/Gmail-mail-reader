package com.myapp.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class Handler {


	private static final boolean DESC = false;

	public static Map<String, Integer> handle(Gmail service, List<Message> messages) throws IOException {
		final Map<String, Integer> allListMap=new HashMap<>();
		BatchRequest b = service.batch();
		//callback function. (Can also define different callbacks for each request, as required)
		JsonBatchCallback<Message> bc = new JsonBatchCallback<Message>() {

			@Override
			public void onSuccess(Message t, HttpHeaders responseHeaders)
					throws IOException {
				HashSet<String> emailId = new HashSet<String>();
				for(int i=0;i<t.getPayload().getHeaders().size();i++){
					String index=t.getPayload().getHeaders().get(i).getName();

					if(index.equals("From")||index.equals("To")||index.equals("Cc")){
						String[] ids=t.getPayload().getHeaders().get(i).getValue().split(",");
						for(int j=0;j<ids.length;j++){
							if(ids[j].contains(">")){
								String emaid_id=ids[j].split(">")[0].split("<")[1].trim();
								emailId.add(emaid_id.toLowerCase());
								//resp.getWriter().println(""+emaid_id);

							}
							else{
								emailId.add(ids[j].trim().toLowerCase());
								//resp.getWriter().println(""+ids[j]);
							}
						}
					}
				}
				Iterator<String> iterator = emailId.iterator(); 
				while (iterator.hasNext()){
					String key=iterator.next();
					if(allListMap.get(key)==null){
						allListMap.put(key, 1);
					}else{
						Integer value =allListMap.get(key);
						allListMap.put(key, value+1);
					} 
				}
			}
			@Override
			public void onFailure(GoogleJsonError e,
					HttpHeaders responseHeaders) throws IOException {
				// TODO Auto-generated method stub

			}


		};

		for (Message message : messages) {
			service.users().messages().get("me", message.getId()).queue(b, bc);
		}
		b.execute();

		Map<String, Integer> sortedMapDesc = sortByComparator(allListMap, DESC);
		return sortedMapDesc;

	}



	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	{

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>()
				{
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}




}



