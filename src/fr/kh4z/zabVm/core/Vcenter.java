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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class Vcenter {
	
	private static Logger logger = LoggerFactory.getLogger("zabVm");
	
	private ServiceInstance si;
	
	private PropertyFile pf;
		
	public Vcenter(){		
		// Chargement du fichier de configuration
		pf = Main.getConfFile();						
	}
    
	public ManagedEntity[] getVirtualMachines(){
		Folder rootFolder = si.getRootFolder();
		// Récuperation des machines virtuelles
		ManagedEntity[] mgEntity = null;
		try {
			mgEntity = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		} catch (InvalidProperty e) {
			logger.error("InvalidProperty:",e);
		} catch (RuntimeFault e) {
			logger.error("RuntimeFault:",e);
		} catch (RemoteException e) {
			logger.error("RemoteException:",e);
		}
		return mgEntity;
	}
	
	public void login(){
		// Login
		String url = "https://" + pf.getVcenterHost() + "/sdk/vimService";
		try {
			si = new ServiceInstance(new URL(url), pf.getVcenterUser(), pf.getVcenterPassword(), true);
		} catch (RemoteException e) {
			//
			logger.error("Impossible de se connecter au vcenter",e);
		} catch (MalformedURLException e) {
			logger.error("URL malformée",e);
		}
	}
       
	public void logout(){
		// Logout
		si.getServerConnection().logout();
	}
}
