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

package org.opensixen.omvc.console.view;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;
import org.opensixen.omvc.console.dialog.AbstractDialog;

/**
 * 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public abstract class AbstractView extends ViewPart implements SelectionListener{

	protected RemoteConsoleProxy console;
		
	private Composite parent;
	private Button addBtn, editBtn, deleteBtn;
	
	protected Table table;

	private Button reloadBtn;
		
	public AbstractView() {
		super();
		this.console = RemoteConsoleProxy.getInstance();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		top.setLayout(layout);
		// Botones
		Composite btnComposite = new Composite(top, SWT.NONE);
		btnComposite.setLayout(new FillLayout());
		reloadBtn = new Button(btnComposite, SWT.PUSH);
		reloadBtn.setText("Recargar");
		reloadBtn.addSelectionListener(this);
		addBtn = new Button(btnComposite, SWT.PUSH);
		addBtn.setText("Nuevo");
		addBtn.addSelectionListener(this);
		editBtn = new Button(btnComposite, SWT.PUSH);
		editBtn.setText("Editar");
		editBtn.addSelectionListener(this);
		deleteBtn = new Button(btnComposite, SWT.PUSH);
		deleteBtn.setText("Eliminar");
		deleteBtn.addSelectionListener(this);
		

		// Tabla
		table = new Table (top, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		// Obtenemos los titulos de la ventana
		String[] titles = getColumnsTitles();
		
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	
		
		// Rellenamos la tabla
		fillTable(table);				
				
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack();
		}			
	}

	
	@Override
	public void setFocus() {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// Añadir
		if (e.getSource().equals(addBtn))	{
			TitleAreaDialog dialog = getNewRecordDialog(parent.getShell());
			if (dialog.open() == SWT.OK)	{
				reload();
			}
			reload();
		}
		// Editar
		else if (e.getSource().equals(editBtn))	{
			int index = table.getSelectionIndex(); 
			if (index == -1)	{
				return;
			}
			AbstractDialog dialog = getEditDialog(parent.getShell(), index);
			
			if (dialog.open() == SWT.OK)	{
				reload();
			}			
			reload();
		}
		// Eliminar
		else if (e.getSource().equals(deleteBtn))	{
			
		}
		// Eliminar
		else if (e.getSource().equals(reloadBtn))	{
			reload();
		}
	
	}
	
	
	/**
	 * Limpia la tabla y llama a fillTable
	 */
	protected void reload()	{
		table.removeAll();
		fillTable(table);
	}
	
	protected abstract AbstractDialog getNewRecordDialog(Shell shell);
	protected abstract AbstractDialog getEditDialog(Shell shell, int index);
	protected abstract String[] getColumnsTitles();
	protected abstract void fillTable(Table table);
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}


	
	
}
