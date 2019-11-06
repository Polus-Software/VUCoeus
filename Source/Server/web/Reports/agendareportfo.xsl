<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:n1="http://irb.mit.edu/irbnamespace" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xsl:variable name="fo:layout-master-set">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
                <fo:region-body margin-top="0.79in" margin-bottom="0.79in" />
            </fo:simple-page-master>
        </fo:layout-master-set>
    </xsl:variable>
    <xsl:template match="/">
        <fo:root>
            <xsl:copy-of select="$fo:layout-master-set" />
            <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:block font-size="24pt" font-weight="bold" space-before.optimum="1pt" space-after.optimum="2pt">
                            <fo:block>
                                <fo:block text-align="center" space-before.optimum="1pt" space-after.optimum="2pt">
                                    <fo:block>Agenda</fo:block>
                                </fo:block>
                            </fo:block>
                        </fo:block>
                        <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:ScheduleMasterData">
                                <xsl:for-each select="n1:CommitteeName">
                                    <fo:block text-align="center" space-before.optimum="1pt" space-after.optimum="2pt">
                                        <fo:block>
                                            <xsl:apply-templates />
                                        </fo:block>
                                    </fo:block>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:ScheduleMasterData">
                                <xsl:for-each select="n1:ScheduledTime">
                                    <fo:block text-align="center" space-before.optimum="1pt" space-after.optimum="2pt">
                                        <fo:block>
                                            <xsl:apply-templates />
                                        </fo:block>
                                    </fo:block>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:ScheduleMasterData">
                                <xsl:for-each select="n1:Place">
                                    <fo:block text-align="center" space-before.optimum="1pt" space-after.optimum="2pt">
                                        <fo:block>
                                            <xsl:apply-templates />
                                        </fo:block>
                                    </fo:block>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <fo:inline font-weight="bold">Agenda Items:</fo:inline>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>1) Review the minutes from <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:PreviousScheduleDate">
                                <xsl:apply-templates />
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>2) The next meeting will be held on <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:NextScheduleDate">
                                <xsl:apply-templates />
                            </xsl:for-each>
                        </xsl:for-each> in <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:NextSchedulePlace">
                                <xsl:apply-templates />
                            </xsl:for-each>
                        </xsl:for-each>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <fo:inline font-weight="bold">Protocols Submitted for Review</fo:inline>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>
                        <fo:block>
                            <fo:leader leader-pattern="space" />
                        </fo:block>&#160;<xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:ProtocolSubmission">
					<xsl:sort select="n1:ProtocolSummary/n1:ProtocolMasterData/n1:ProtocolTitle"/>
                                <xsl:for-each select="n1:ProtocolSummary">
                                    <fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt">
                                        <fo:table-column />
                                        <fo:table-column />
                                        <fo:table-column />
                                        <fo:table-body>
							<xsl:for-each select="n1:ProtocolMasterData">
                                                <fo:table-row>
                                                    <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                        <fo:block>
                                                            <fo:inline font-weight="bold">Protocol number:</fo:inline>&#160;<xsl:for-each select="n1:ProtocolNumber">
                                                                <xsl:apply-templates />
                                                            </xsl:for-each>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                        <fo:block>
                                                            <fo:inline font-weight="bold">Type</fo:inline>: <xsl:for-each select="n1:ProtocolTypeDesc">
                                                                <xsl:apply-templates />
                                                            </xsl:for-each>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                        <fo:block>
                                                            <fo:inline font-weight="bold">Title</fo:inline>: <xsl:for-each select="n1:ProtocolTitle">
                                                                <xsl:apply-templates />
                                                            </xsl:for-each>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each>
                                        </fo:table-body>
                                    </fo:table>
                                </xsl:for-each>
                                <fo:block>
                                    <fo:leader leader-pattern="space" />
                                </fo:block>
                                <xsl:for-each select="n1:ProtocolSummary">
                                    <fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt">
                                        <fo:table-column />
                                        <fo:table-header>
                                            <fo:table-row>
                                                <fo:table-cell height="19pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                    <fo:block>Investigators</fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-header>
                                        <fo:table-body>
                                            <xsl:for-each select="n1:Investigator">
                                                <fo:table-row>
                                                    <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                        <fo:block>
                                                            <fo:block start-indent="1em" end-indent="1em" text-align="left" space-before.optimum="1pt" space-after.optimum="2pt">
                                                                <fo:block>
                                                                    <xsl:for-each select="n1:Person">
                                                                        <xsl:for-each select="n1:Fullname">
                                                                            <xsl:apply-templates />
                                                                        </xsl:for-each>
                                                                    </xsl:for-each>
                                                                </fo:block>
                                                            </fo:block>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:for-each>
                                        </fo:table-body>
                                    </fo:table>
                                </xsl:for-each>
                                <fo:block>
                                    <fo:leader leader-pattern="space" />
                                </fo:block>
                            </xsl:for-each>
                        </xsl:for-each>
                        <xsl:for-each select="n1:Schedule">
                            <xsl:for-each select="n1:ProtocolSubmission" />
                        </xsl:for-each>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
