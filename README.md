ZabVm
=======
Automaticaly import VM into Zabbix (using Vmware API and Zabbix API).

Configuration:
============
Create a configuration directory and create a properties files (you can have multiples properties files):
- example:      
  - /etc/zabvm/vcenter1.properties
  - /etc/zabvm/vcenter2.properties

Containing:

  # Vcenter  
  vcenter.host=192.168.1.1  
  vcenter.user=zabbix  
  vcenter.password=changeit 
  
  # VM  
  vm.name.regexp=^([a-zA-Z0-9]+).*  
  vm.name.toLowerCase=true  
  vm.name.exclude=Template
  
  # Zabbix API  
  zabbixAPI.url=https://zabbix.mydomain.lan/api_jsonrpc.php  
  zabbixAPI.user=read  
  zabbixAPI.password=changeit


Usage:
=====

Run:
- ./zabvm -c /path/toDirectoryConfig/

