package org.eclipse.epsilon.eol.viatra.engine;

public class LocalSearchEngineOptions {
	
	public static final LocalSearchEngineOptions DISABLED = new LocalSearchEngineOptions();
	
	private final boolean localSearchEnabled;
	private final boolean useBaseIndex;
	
	private LocalSearchEngineOptions() {
		this.localSearchEnabled = false;
		this.useBaseIndex = false;
	}
	
	protected LocalSearchEngineOptions(Builder builder) {
		this.localSearchEnabled = builder.localSearchEnabled;
		this.useBaseIndex = builder.useBaseIndex;
	}
	
	public boolean isLocalSearchEnabled() {
		return localSearchEnabled;
	}
	
	public boolean useBaseIndex() {
		return useBaseIndex;
	}
	
	public static class Builder {
		private boolean localSearchEnabled;
		private boolean useBaseIndex;
		
		public Builder enableLocalSearch(boolean isEnabled) {
			this.localSearchEnabled = isEnabled;
			return this;
		}
		
		public Builder useBaseIndex(boolean useBaseIndex) {
			this.useBaseIndex = useBaseIndex;
			return this;
		}
		
		public LocalSearchEngineOptions build() {
			return new LocalSearchEngineOptions(this);
		}
	}

}
