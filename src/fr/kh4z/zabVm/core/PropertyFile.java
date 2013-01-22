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

package fr.kh4z.zabVm.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import lombok.Getter;
import lombok.ToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter

@ToString

//Suppression des warnings SONAR
@SuppressWarnings("PMD.UnusedPrivateField")

public final class PropertyFile {	

    private String vcenterHost;

    private String vcenterUser;

    private String vcenterPassword;

    private String vmNameRegexp;

    private boolean vmNameToLowerCase;

    private String vmNameExclude;

    private String zabbixAPIUrl;

    private String zabbixAPIUser;

    private String zabbixAPIPassword;

    private static Logger logger = LoggerFactory.getLogger("zabVm");

    public PropertyFile(String path){
        Properties props = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream(path);
            try {

                // Loading du fichier de conf
                props.load(fis);

                // Vcenter (VimService)

                vcenterHost = props.getProperty("vcenter.host");

                vcenterUser = props.getProperty("vcenter.user");

                vcenterPassword = props.getProperty("vcenter.password");

                vmNameRegexp = props.getProperty("vm.name.regexp");

                vmNameToLowerCase = Boolean.parseBoolean(props.getProperty("vm.name.toLowerCase"));

                vmNameExclude = props.getProperty("vm.name.exclude");

                // ZabbixAPI

                zabbixAPIUrl = props.getProperty("zabbixAPI.url");

                zabbixAPIUser = props.getProperty("zabbixAPI.user");

                zabbixAPIPassword = props.getProperty("zabbixAPI.password");

            } catch (IOException e) {
                logger.error("Impossible de lire le fichier: {}",e);
            }
        } catch (FileNotFoundException e) {
            logger.error("Fichier inexistant: {}",e);
        }
    }
	
}
