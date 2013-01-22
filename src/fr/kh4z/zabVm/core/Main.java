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

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.beust.jcommander.JCommander;
import fr.kh4z.zabVm.zabbixApi.ZabbixApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public final class Main {
	
	private static Logger logger = LoggerFactory.getLogger("zabVm");
	
	private static PropertyFile pf;

	private static ZabbixApiClient zabbixApi;

	private Main(){}

	public static void main(String[] args){

        // Parsing des arguments
        Args arguments = new Args();
        new JCommander(arguments, args);

        // Récuperation des fichiers properties
        File[] fList =  listFiles(arguments.getConfigurationDirectory());

        // Pour chaque fichier de conf
        for (File file : fList){
            if (file.isFile()){

                // Initialisation du fichier de conf
                pf = new PropertyFile(file.getPath());
                logger.info("Fichier de properties: {}",file.getName());

                // Connexion à l'api du vcenter
                Vcenter vcenter = new Vcenter();
                vcenter.login();

                logger.debug("Connexion à l'API Zabbix");
                zabbixApi = new ZabbixApiClient();

                execute(vcenter.getVirtualMachines());

                // Logout
                vcenter.logout();
            }
        }
    }

    private static void execute(ManagedEntity[] mgEntity){
        for(int i = 0; i<  mgEntity.length; i++){
            VirtualMachine vm = (VirtualMachine) mgEntity[i];
            if(!vm.getName().contains(pf.getVmNameExclude())){
                VmCreateHost vmCreate = new VmCreateHost((VirtualMachine) mgEntity[i]);
                vmCreate.execute();
            }
        }
    }

    private static File[] listFiles(String directoryName){

        File dir = new File(directoryName);

        File[] fList = dir.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }});
        return fList;
    }

    public static String findVmName(VirtualMachine vm){
        String hostname;
        // Si les VMwareTools ne sont pas installés, on se base sur le nom récuperé depuis l'inventaire
        if(vm.getGuest().getHostName() ==null){
             hostname = getCleanHostname(vm.getName());
        }else{
            // Récuperation du FQDN
            hostname = getCleanHostname(vm.getGuest().getHostName());
        }
        return hostname;
    }

    private static String getCleanHostname(String vmName){
        // Formate le nom de la machine virtual selon l'expression reguliere spécifié dans le fichier de configuration
        Pattern p = Pattern.compile(pf.getVmNameRegexp());
        Matcher m = p.matcher(vmName);
        // Extraction du nom sinon retourne une chaine vide
        String vmCleanName = m.find() ? m.group(1) : "";
        return pf.isVmNameToLowerCase() ? vmCleanName.toLowerCase() : vmCleanName;
    }

    // Acces au fichier de conf courant
    public static PropertyFile getConfFile(){
        return(pf);
    }
}
