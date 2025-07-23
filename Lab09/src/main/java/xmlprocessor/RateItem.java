package xmlprocessor;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class RateItem {

    @XmlElement(name = "targetCurrency")
    private String targetCurrency;

    @XmlElement(name = "exchangeRate")
    private BigDecimal exchangeRate;

    @XmlElement(name = "inverseRate")
    private BigDecimal inverseRate;

    private String date;

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getInverseRate() {
        return inverseRate;
    }

    public void setInverseRate(BigDecimal inverseRate) {
        this.inverseRate = inverseRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}