module metrics {
    requires lib;
    exports metrics;
    provides ex.api.AnalysisService with metrics.SimpleMetricsService;
}
