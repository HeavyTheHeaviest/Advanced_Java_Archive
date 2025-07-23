<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">

    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:template match="/channel">
        <xsl:for-each select="item">
            <xsl:value-of select="targetCurrency"/>
            <xsl:text> | kurs: </xsl:text><xsl:value-of select="exchangeRate"/>
            <xsl:text> | odwrotny: </xsl:text><xsl:value-of select="inverseRate"/>
            <xsl:text> | data: </xsl:text><xsl:value-of select="pubDate"/>
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="text()|@*"/>
</xsl:stylesheet>
