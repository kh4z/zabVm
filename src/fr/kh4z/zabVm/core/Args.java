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

import com.beust.jcommander.Parameter;
import lombok.Getter;

@Getter

public class Args {
    @Parameter(names = { "-c", "--configurationDirectory" }, description = "Configuration Directory", required = true)
    private String configurationDirectory;
}
