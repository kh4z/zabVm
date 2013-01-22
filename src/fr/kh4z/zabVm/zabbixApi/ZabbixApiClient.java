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


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Random;

public class ZabbixApiClient extends ZabbixApi {

    private PropertyFile pf;

    private static final int MINID = 1;

    private static final int MAXID = 10000;

    private static Logger logger = LoggerFactory.getLogger("zabVm");

    public ZabbixApiClient(){
        // Récuperation d'un ID aléatoire
        id = getRandomId();
        // Récuperation du fichier de conf
        pf = Main.getConfFile();
        try {
            login();
        } catch (JSONException e) {
            logger.error("JSON exception", e);
        }
    }

    private void login() throws JSONException {

        String user = pf.getZabbixAPIUser();
        String password = pf.getZabbixAPIPassword();

        JSONObject jsonObj=new JSONObject("{\"jsonrpc\":\"2.0\"," +
                "\"method\":\"user.login\"," +
                "\"params\": { \"user\": \""+ user +"\", \"password\": \"" + password +"\"}," +
                "\"id\":"+ id +"}");
        // recuperation du hash auth.
        auth = sendToApi(jsonObj);
    }

    public void logout() throws JSONException {
        // A tester en 2.0
        String body ="\"method\":\"user.logout\",";
        sendToApi(body);
    }

    private int getRandomId(){
        Random rand = new Random();
        return rand.nextInt(MAXID - MINID + 1) + MINID;
    }
}
