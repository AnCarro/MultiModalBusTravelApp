 public void GetTimeToDest(LatLng dest, String mode) {
        //mode can be driving, walking, bicycling, transit
       String url = "https://maps.googleapis.com/maps/api/distancematrix/json?parameters?";
       String origins = String.valueOf(curLoc.latitude) +","+String.valueOf(curLoc.longitude);
       String destinations= String.valueOf(dest.latitude) +","+String.valueOf(dest.longitude);
       String travelMode = mode;
       String units = "imperial";
       String key = "AIzaSyD1XgaLSdCgTMe9tOp4EBSSkETH-KsYL3g";
       String DirReq = url+"units="+units+"&origins="+origins+"&destinations="+destinations+"&mode="+mode+"&key="+key;
       int SendMethod = Request.Method.GET;
       System.out.println("DirReq: " + DirReq);
       RequestQueue Queue = ServerRequestsQueue.getInstance(context.getApplicationContext()).getRequestQueue();
       JsonObjectRequest JOR = new JsonObjectRequest(SendMethod, DirReq, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                //System.out.println("Response destination is: " + resp.toString());
                try {
                    JSONArray rows = resp.getJSONArray("rows");
                        //System.out.println("rows: " + rows);
                        JSONObject row = rows.getJSONObject(0);
                        JSONObject values = row.getJSONArray("elements").getJSONObject(0);
                        System.out.println("values: " + values);
                            JSONObject distance = values.getJSONObject("distance");
                                String distText = distance.getString("text");
                                String distValue = distance.getString("value");
                            JSONObject duration = values.getJSONObject("duration");
                                String durText = duration.getString("text");
                                String durValue = duration.getString("value");

                    bottomTextView.setText("Distance: " + distText + "\n" +
                            "Time: " + durText);
/*                    bottomTextView.setVisibility(View.VISIBLE);
                    bottomTextView.setHeight(80);
                    ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                    params.height = 1000;
                    mapFragment.getView().setLayoutParams(params);*/

                 //   String elements = dist.getString("elements");
                //    System.out.println("elements: " + elements);
                 //   System.out.println("Dist: " + dist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ouch, Failure: " + error.getLocalizedMessage() + " " + error.getMessage());
            }
        });
        Queue.add(JOR);
    }