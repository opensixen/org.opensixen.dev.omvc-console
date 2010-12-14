 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.omvc.console.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensixen.omvc.console.ConfigUtil;
import org.opensixen.riena.client.proxy.RienaServerProxy;

/**
 * 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class ConfigDialog extends AbstractDialog {

	private ConfigUtil config;
	private Text fHost, fUser, fPassword, fPort, fService;
	
	public ConfigDialog(Shell parentShell, ConfigUtil config) {
		super(parentShell);
		this.config = config;
	}
	
	@Override
	public void create() {
		super.create();
		setTitle("Configuracion");
		setMessage("Edite sus opciones de configuracion.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
		Label l = new Label(parent, SWT.NONE);
		l.setText("Host");				
		fHost = new Text(parent, SWT.BORDER);
		if (config.getHost() != null)	{
			fHost.setText(config.getHost());
		}
		l = new Label (parent, SWT.NONE);
		l.setText("Puerto");
		fPort = new Text(parent, SWT.BORDER);
		if (config.getPort() != null)	{
			fPort.setText(config.getPort());
		}
		else {
			fPort.setText("8080");
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Servicio");
		fService = new Text(parent, SWT.BORDER);
		if (config.getService() != null)	{
			fService.setText(config.getService());
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Usuario");
		fUser = new Text(parent, SWT.BORDER);
		if (config.getUser() != null)	{
			fUser.setText(config.getUser());
		}
		
		l = new Label(parent, SWT.NONE);
		l.setText("Password");
		fPassword = new Text(parent, SWT.BORDER);
		fPassword.setEchoChar('*');
		if (config.getPassword() != null)	{
			fPassword.setText(config.getPassword());
		}
		
		
		
		return parent;
	}

	
	@Override
	protected boolean isValidInput() {
		
		if (fHost.getText() != null && fPort.getText() != null && fUser.getText() != null && fPassword.getText() != null)	{
			return true;	
		}
		return false;
		
	}

	@Override
	protected void saveInput() {
		config.setHost(fHost.getText());
		config.setPort(fPort.getText());
		config.setService(fService.getText());
		config.setUser(fUser.getText());
		config.setPassword(fPassword.getText());
		config.saveConf();
		RienaServerProxy.restartConnection();
	}

}
