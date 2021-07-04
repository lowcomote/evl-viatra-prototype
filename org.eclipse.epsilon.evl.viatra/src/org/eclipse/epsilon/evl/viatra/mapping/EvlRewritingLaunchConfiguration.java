package org.eclipse.epsilon.evl.viatra.mapping;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dt.launching.EpsilonLaunchConfigurationDelegateListener;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.staticanalyser.EvlStaticAnalyser;

public class EvlRewritingLaunchConfiguration implements EpsilonLaunchConfigurationDelegateListener {

	@Override
	public void aboutToParse(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor progressMonitor, IEolModule module) throws CoreException {

	}

	@Override
	public void aboutToExecute(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor progressMonitor, IEolModule module) throws Exception {
		if (module instanceof EvlModule) {
			module = (EvlModule) module;
			module.getContext().setModule(module);
			EvlStaticAnalyser staticAnlayser = new EvlStaticAnalyser();

			staticAnlayser.validate(module);

			new EvlRewritingHandler().invokeRewriters((EvlModule) module, staticAnlayser);

			// System.err.println(new EvlUnparser().unparse((EvlModule) module));
		}

	}

	@Override
	public void executed(ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor progressMonitor, IEolModule module, Object result) throws Exception {

	}

}
