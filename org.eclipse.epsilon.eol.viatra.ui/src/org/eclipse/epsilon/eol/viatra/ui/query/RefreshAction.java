package org.eclipse.epsilon.eol.viatra.ui.query;

import org.eclipse.epsilon.eol.viatra.ui.Activator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class RefreshAction extends Action implements IAction {
   protected QueryRewritingView queryView;
	
	public RefreshAction(QueryRewritingView queryView) {
		setText("Refresh");
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/refresh.gif"));
		this.queryView = queryView;
	}
	
	@Override
	public void run() {
		queryView.render(queryView.getEditor());
	}
}
