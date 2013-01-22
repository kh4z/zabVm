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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter

@Setter

@ToString

public class ApiHost extends ZabbixApi {

    private long groupId;

    private String hostname;

    private String ip;

    private String useIp;

    private String templates;

    private String fqdn;

    public String exists() {
        return sendToApi("\"method\":\"host.exists\"," +
                "\"params\": { " +
                "\"host\": \""+ hostname +"\" " +
                "},");
    }

    public String create() {
        return sendToApi("\"method\":\"host.create\"," +
                "\"params\": { " +
                    "\"host\": \""+ hostname +"\", " +
                    "\"ip\": \"" + ip +"\"," +
                    "\"useip\": \"" + useIp +"\"," +
                    "\"dns\": \"" + fqdn +"\"," +
                    "\"groups\": {"+
                        "\"groupid\":\""+ groupId +"\"},"+
                    "\"templates\": [{"+
                        "\"templateid\":\""+ templates +"\"}],"+
                "},");

    }

    public String create(String hostname, String ip, String useIp, String fqdn,long groupId, String templates) {
        return sendToApi("\"method\":\"host.create\"," +
                "\"params\": { " +
                "\"host\": \""+ hostname +"\", " +
                "\"ip\": \"" + ip +"\"," +
                "\"useip\": \"" + useIp +"\"," +
                "\"dns\": \"" + fqdn +"\"," +
                "\"groups\": {"+
                "\"groupid\":\""+ groupId +"\"},"+
                "\"templates\": [{"+
                "\"templateid\":\""+ templates +"\"}],"+
                "},");
    }
}
