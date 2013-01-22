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

/*
* Permet la création automatique des VM dans Zabbix
*
*/

import com.vmware.vim25.mo.VirtualMachine;
import fr.kh4z.zabVm.zabbixApi.ApiHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VmCreateHost {

    private static Logger logger = LoggerFactory.getLogger("zabVm");

    private final static long DISCOVEREDHOSTS = 100100000000020L;

    VirtualMachine vm;

    VmCreateHost(VirtualMachine vm){
        this.vm = vm;
    }

    public void execute(){

        ApiHost host = new ApiHost();

        // Récuperation du nom
        host.setHostname(Main.findVmName(vm));
        // Si la VM n'existe pas dans plugin & Si la VM n'est pas un template
        if(!Boolean.valueOf(host.exists())){
            // Configuration du host:
            host.setUseIp("1");
            host.setIp(vm.getGuest().getIpAddress());
            host.setFqdn(vm.getGuest().getHostName().toLowerCase());
            host.setGroupId(DISCOVEREDHOSTS);
            host.setTemplates(getHostTemplate(vm.getGuest().getGuestFullName()));

            // Envoi à l'API
            logger.info("Création de la machine: {}",host.toString());
            host.create();
        }
    }

    private String getHostTemplate(String guestOS){
        // A revoir
        if(guestOS.contains("linux")){
            return "100100000010059";
        }else if (guestOS.contains("windows")){
            return "100100000010128";
        }else{
            // template linux par defaut
            return "100100000010059";
        }
    }

}
