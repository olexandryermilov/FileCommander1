<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <html>
            <head><title>Software List</title></head>
            <body>
                <h2>Software details:</h2>
                <table border="1">
                    <tr bgcolor="#9acd32">
                        <th>Name</th>
                        <th>Description</th>
                        <th>Version</th>
                        <th>Type</th>
                        <th>Author</th>
                        <th>License</th>
                    </tr>
                    <xsl:for-each select="facultySoftwareDatabase/software">
                        <tr>
                            <td><xsl:value-of select="@name"/></td>
                            <td><xsl:value-of select="@description"/></td>
                            <td><xsl:value-of select="@version"/></td>
                            <td><xsl:value-of select="type"/></td>
                            <td><xsl:value-of select="author"/></td>
                            <td><xsl:value-of select="license"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>