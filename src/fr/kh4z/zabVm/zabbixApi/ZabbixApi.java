/* This file is part of zabVm.
*
* zabVm is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* zabVm is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*
* You should have received a copy of the GNU General Public License along with
* zabVm. If not, see <http://www.gnu.org/licenses/>.
*/

package fr.kh4z.zabVm.zabbixApi;

import fr.kh4z.zabVm.core.Main;
import fr.kh4z.zabVm.core.PropertyFile;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ZabbixApi {

    private static Logger logger = LoggerFactory.getLogger("zabVm");

    protected static String auth;

    protected static int id;

    public ZabbixApi(){}

    protected String sendToApi(String body){
        RequestAPI reqApi = new RequestAPI();
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject("{\"jsonrpc\":\"2.0\","+
                    body +
                    "\"id\":"+ id +",\"auth\": \""+ auth + "\"}");

        } catch (JSONException e) {
            logger.error("JSon malformé:",e);
        }
        return reqApi.send(jsonObj);
    }

    protected String sendToApi(JSONObject json){
        RequestAPI reqApi = new RequestAPI();
        return reqApi.send(json);
    }

    final class RequestAPI {

        private PropertyFile pf;

        public RequestAPI(){
            pf = Main.getConfFile();
        }

        String send(JSONObject jsonObj) {

            HttpClient client = new HttpClient();

            PutMethod putMethod = new PutMethod(pf.getZabbixAPIUrl());
            putMethod.setRequestHeader("Content-Type", "application/json-rpc");
            try {
                putMethod.setRequestBody(fromString(jsonObj.toString()));
            } catch (UnsupportedEncodingException e) {
                logger.error("EncodingException",e);
            }

            logger.debug("jsonObj {}", jsonObj.toString());

            String response;

            try {
                client.executeMethod(putMethod); // send to request to the plugin api

                response = putMethod.getResponseBodyAsString(); // read the result of the response

                logger.debug("Response: {}",response);

                // Work with the data using methods like...
                JSONObject obj;
                try {
                    obj = new JSONObject(response);
                    return obj != null ? obj.getString("result") : "error";
                } catch (JSONException e) {
                    logger.error("JSONException:",e);
                }
            } catch (HttpException e) {
                logger.error("httpException:", e);
            } catch (IOException e) {
                logger.error("IoException:", e);
            }
            return "";
        }
        private InputStream fromString(String str) throws UnsupportedEncodingException {
            byte[] bytes = str.getBytes("UTF-8");
            return new ByteArrayInputStream(bytes);
        }
    }

}
