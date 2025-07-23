module app {
    requires java.desktop; // wymagane przez Swinga
    requires lib;
    requires metrics;       // moduł z AnalysisService, DataSet, AnalysisException
    uses ex.api.AnalysisService; // aplikacja korzysta z mechanizmu SPI dla AnalysisService
}