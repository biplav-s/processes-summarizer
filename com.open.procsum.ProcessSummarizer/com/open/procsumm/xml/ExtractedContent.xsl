<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html"/>

<xsl:template match="contents">
  <html><body>
    <xsl:apply-templates/>
  </body></html>
</xsl:template>


<xsl:template match="extractedContent">
  <xsl:apply-templates/>
  <hr></hr>
</xsl:template>

<xsl:template match="originModelReference">
  <b><xsl:apply-templates/></b><br/>
</xsl:template>

<xsl:template match="modelReference">
  <b><xsl:apply-templates/></b><br/>
</xsl:template>

<xsl:template match="singleContent">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="contentList">
  <ul><xsl:apply-templates/><br/></ul>
</xsl:template>

<xsl:template match="singleContentInList">
  <li><xsl:apply-templates/></li>
</xsl:template>

<xsl:template match="repeatedContent">
  <table border="1"><xsl:apply-templates/></table>
</xsl:template>


<xsl:template match="aRow">
  <tr><xsl:apply-templates/></tr>
</xsl:template>


<xsl:template match="aCol">
  <td><xsl:apply-templates/></td>
</xsl:template>

</xsl:stylesheet>