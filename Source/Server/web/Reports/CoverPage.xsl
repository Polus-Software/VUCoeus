<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:n1="http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" xmlns:n2="http://era.nih.gov/Projectmgmt/SBIR/CGAP/phs398.namespace" xmlns:n3="http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace">
    <xsl:variable name="fo:layout-master-set">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.4in" margin-right="1.4in">
                <fo:region-body margin-top="0.2in" margin-bottom="0.2in" />
            </fo:simple-page-master>
        </fo:layout-master-set>
    </xsl:variable>
    <xsl:template match="/">
        <fo:root>
            <xsl:copy-of select="$fo:layout-master-set" />
            <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="8pt">
                        <fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt">
                            <fo:table-column column-width="148pt" />
                            <fo:table-column column-width="69pt" />
                            <fo:table-column column-width="55pt" />
                            <fo:table-column column-width="59pt" />
                            <fo:table-column column-width="115pt" />
                            <fo:table-column column-width="115pt" />
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" text-align="left" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Department of Health and Human Services<fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Public Health Services</fo:block>
                                            </fo:block>
                                            <fo:inline font-size="10pt" font-weight="bold">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Grant Application</fo:inline>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:inline font-size="8pt">LEAVE BLANK&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; </fo:inline>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="6" width="650pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>1. TITLE OF PROJECT</fo:block>
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="ProjectTitle">
                                                        <fo:inline font-weight="bold">
                                                            <xsl:apply-templates />
                                                        </fo:inline>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="6" width="650pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>2. RESPONSE TO SPECIFIC REQUEST FOR APPLICATIONS OR PROGRAM ANNOUNCEMENT OR SOLICITATION&#160; <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="FundingOpportunityDetails">
                                                        <xsl:for-each select="FundingOpportunityResponseCode">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='false'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='0'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>NO&#160;&#160; <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="FundingOpportunityDetails">
                                                        <xsl:for-each select="FundingOpportunityResponseCode">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='true'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='1'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>&#160; YES&#160; <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>(If &quot;Yes&quot;, state number and title)<fo:block>
                                                        <fo:leader leader-pattern="space" />
                                                    </fo:block>Number:<xsl:for-each select="n1:ResearchAndRelatedProject">
                                                        <xsl:for-each select="n1:ResearchCoverPage">
                                                            <xsl:for-each select="FundingOpportunityDetails">
                                                                <xsl:for-each select="FundingOpportunityNumber">
                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                        <xsl:apply-templates />
                                                                    </fo:inline>
                                                                </xsl:for-each>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>&#160;&#160; Title: <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                        <xsl:for-each select="n1:ResearchCoverPage">
                                                            <xsl:for-each select="FundingOpportunityDetails">
                                                                <xsl:for-each select="FundingOpportunityTitle">
                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                        <xsl:apply-templates />
                                                                    </fo:inline>
                                                                </xsl:for-each>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </fo:block>
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:inline font-weight="bold">3. PRINCIPAL INVESTIGATOR/PROGRAM DIRECTOR</fo:inline>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:inline font-weight="bold">New Investigator </fo:inline>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="n1:ProgramDirectorPrincipalInvestigator">
                                                        <xsl:for-each select="NewInvestigatorQuestion">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='false'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='0'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline> No</xsl:for-each>
                                                        <xsl:for-each select="NewInvestigatorQuestion">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='true'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='1'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline> Yes</xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>&#160;</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3a. NAME (Last, first, middle)<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="n1:ProgramDirectorPrincipalInvestigator">
                                                        <xsl:for-each select="Name">
                                                            <xsl:for-each select="LastName">
                                                                <fo:inline font-size="10pt" font-weight="bold">
                                                                    <xsl:apply-templates />
                                                                </fo:inline>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3b. DEGREE(S)<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3c. POSITION TITLE<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>&#160;&#160; (not yet)<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" number-rows-spanned="4" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3d. MAILING ADDRESS (Street, city, state, zip code) <fo:inline font-weight="bold">(using office address right now)</fo:inline>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="n1:ProgramDirectorPrincipalInvestigator">
                                                        <xsl:for-each select="ContactInformation">
                                                            <xsl:for-each select="PostalAddress">
                                                                <xsl:for-each select="City">
                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                        <xsl:apply-templates />
                                                                    </fo:inline>
                                                                </xsl:for-each>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <xsl:text>&#xA;</xsl:text>
                                            </fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>77 Massachusetts Avenue</fo:block>
                                            </fo:block>Cambridge, MA 02139<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>E-MAIL ADDRESS<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="n1:ProgramDirectorPrincipalInvestigator">
                                                        <xsl:for-each select="ContactInformation">
                                                            <xsl:for-each select="Email">
                                                                <fo:inline font-size="10pt" font-weight="bold">
                                                                    <xsl:apply-templates />
                                                                </fo:inline>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3e. DEPARTMENT, SERVICE, LABORATORY, OR EQUIVALENT</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3f. MAJOR SUBDIVISION</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>3g. TELEPHONE AND FAX (Area code, number and extension)<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>TEL:&#160; <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="n1:ProgramDirectorPrincipalInvestigator">
                                                        <xsl:for-each select="ContactInformation">
                                                            <xsl:for-each select="PhoneNumber">
                                                                <fo:inline font-size="10pt" font-weight="bold">
                                                                    <xsl:apply-templates />
                                                                </fo:inline>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>&#160;&#160;&#160; FAX:<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="380pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>4. HUMAN SUBJECTS RESEARCH<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ProjectDescription">
                                                    <xsl:for-each select="n1:HumanSubject">
                                                        <xsl:for-each select="HumanSubjectsUsedQuestion">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='false'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='0'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>No<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ProjectDescription">
                                                    <xsl:for-each select="n1:HumanSubject">
                                                        <xsl:for-each select="HumanSubjectsUsedQuestion">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='true'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='1'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>Yes<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>5. VERTEBRATE ANIMALS <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ProjectDescription">
                                                    <xsl:for-each select="n3:AnimalSubject">
                                                        <xsl:for-each select="VertebrateAnimalsUsedQuestion">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='false'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='0'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each> No<xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ProjectDescription">
                                                    <xsl:for-each select="n3:AnimalSubject">
                                                        <xsl:for-each select="VertebrateAnimalsUsedQuestion">
                                                            <fo:inline padding-before="-3pt" padding-after="-2pt" text-decoration="underline" color="black">
                                                                <fo:inline>
                                                                    <xsl:choose>
                                                                        <xsl:when test=".='true'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:when test=".='1'">
                                                                            <fo:inline white-space-collapse="false" font-family="ZapfDingbats" font-size="10pt" padding-start="1pt" padding-end="1pt">&#x2714;</fo:inline>
                                                                        </xsl:when>
                                                                        <xsl:otherwise>
                                                                            <fo:inline text-decoration="underline" color="black">
                                                                                <fo:leader leader-length="8pt" leader-pattern="rule" />
                                                                            </fo:inline>
                                                                        </xsl:otherwise>
                                                                    </xsl:choose>
                                                                </fo:inline>
                                                            </fo:inline>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each> Yes<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="222pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>6. DATES OF PROPOSED PERIOD OF SUPPORT</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="103pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>7. COSTS REQUESTED FOR INITIAL BUDGET PERIOD<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" width="245pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>8. COSTS REQUESTED FOR PROPOSED PERIOD OF SUPPORT<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" width="148pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>From<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="ProjectDates">
                                                        <xsl:for-each select="ProjectStartDate">
                                                            <fo:inline font-size="inherited-property-value(&apos;font-size&apos;) + 4pt" />
                                                            <xsl:value-of select="format-number(substring(., 6, 2), '00')" />
                                                            <xsl:text>/</xsl:text>
                                                            <xsl:value-of select="format-number(substring(., 9, 2), '00')" />
                                                            <xsl:text>/</xsl:text>
                                                            <xsl:value-of select="format-number(substring(., 1, 4), '0000')" />
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" width="69pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>Through<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="ProjectDates">
                                                        <xsl:for-each select="ProjectEndDate">
                                                            <fo:inline font-size="inherited-property-value(&apos;font-size&apos;) + 4pt" />
                                                            <xsl:value-of select="format-number(substring(., 6, 2), '00')" />
                                                            <xsl:text>/</xsl:text>
                                                            <xsl:value-of select="format-number(substring(., 9, 2), '00')" />
                                                            <xsl:text>/</xsl:text>
                                                            <xsl:value-of select="format-number(substring(., 1, 4), '0000')" />
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" width="55pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>7a. Direct Costs ($)<xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="BudgetSummary">
                                                    <xsl:for-each select="BudgetPeriod">
                                                        <xsl:for-each select="PeriodDirectCostsTotal">
                                                            <xsl:if test="../BudgetPeriodID =1">
                                                                <xsl:value-of select="format-number(., '###,##0.00')" />
                                                            </xsl:if>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" width="59pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>7b. Total Costs ($)<xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="BudgetSummary">
                                                    <xsl:for-each select="InitialBudgetTotals">
                                                        <xsl:for-each select="FederalCost">
                                                            <xsl:value-of select="format-number(., '###,##0.00')" />
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" width="115pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>8a. Direct Costs ($)<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <xsl:text>&#xA;</xsl:text>
                                            </fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" width="115pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>8b. Total Costs ($)<xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="BudgetSummary">
                                                    <xsl:for-each select="AllBudgetTotals">
                                                        <xsl:for-each select="FederalCost">
                                                            <xsl:value-of select="format-number(., '###,##0.00')" />
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" number-rows-spanned="2" text-align="left" width="222pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>9.&#160; APPLICANT ORGANIZATION</fo:block>
                                            </fo:block>Name&#160; <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>&#160;&#160;&#160; <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                        <xsl:for-each select="n1:ResearchCoverPage">
                                                            <xsl:for-each select="ApplicantOrganization">
                                                                <xsl:for-each select="OrganizationName">
                                                                    <fo:inline font-size="l10pt" font-weight="bold">
                                                                        <xsl:apply-templates />
                                                                    </fo:inline>
                                                                </xsl:for-each>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </fo:block>
                                            </fo:block>Address<fo:block>
                                                <xsl:text>&#xA;</xsl:text>
                                            </fo:block>
                                            <fo:block text-align="center" space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block text-align="left">&#160;&#160;&#160; <fo:block>
                                                        <fo:leader leader-pattern="space" />
                                                    </fo:block>
                                                    <fo:block>
                                                        <xsl:text>&#xA;</xsl:text>
                                                    </fo:block>
                                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                        <fo:block text-align="left" />
                                                    </fo:block>&#160; &#160; </fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="367pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>10. TYPE OF ORGANIZATION <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>&#160;&#160;&#160;&#160;&#160;&#160; Public:&#160;&#160;&#160;&#160;&#160; --&gt;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Federal&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; State&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Local<fo:block>
                                                        <fo:leader leader-pattern="space" />
                                                    </fo:block>&#160;&#160;&#160;&#160;&#160;&#160; Private:&#160;&#160;&#160;&#160; --&gt;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Private Nonprofit<fo:block>
                                                        <fo:leader leader-pattern="space" />
                                                    </fo:block>&#160;&#160;&#160;&#160;&#160;&#160; For-profit&#160; --&gt;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; General&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Small Business</fo:block>
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="367pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>11.&#160; ENTITY IDENTIFICATION NUMBER<fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                                <fo:leader leader-pattern="space" />
                                            </fo:block>DUNS NO.&#160; <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                <xsl:for-each select="n1:ResearchCoverPage">
                                                    <xsl:for-each select="ApplicantOrganization">
                                                        <xsl:for-each select="OrganizationDUNS">
                                                            <xsl:apply-templates />
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>Congressional District&#160; <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                        <xsl:for-each select="n1:ResearchCoverPage">
                                                            <xsl:for-each select="ApplicantOrganization">
                                                                <xsl:for-each select="OrganizationCongressionalDistrict">
                                                                    <xsl:apply-templates />
                                                                </xsl:for-each>
                                                            </xsl:for-each>
                                                        </xsl:for-each>
                                                    </xsl:for-each>
                                                </fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" text-align="left" width="222pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>12. ADMINISTRATIVE OFFICIAL TO BE NOTIFIED IF AWARD IS MADE <fo:block>
                                                        <fo:leader leader-pattern="space" />
                                                    </fo:block>
                                                    <fo:block>
                                                        <xsl:text>&#xA;</xsl:text>
                                                    </fo:block>
                                                    <fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt">
                                                        <fo:table-column column-width="64pt" />
                                                        <fo:table-column />
                                                        <fo:table-body>
                                                            <fo:table-row>
                                                                <fo:table-cell width="64pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>Name</fo:block>
                                                                </fo:table-cell>
                                                                <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>
                                                                        <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                                            <xsl:for-each select="n1:ResearchCoverPage">
                                                                                <xsl:for-each select="ApplicantOrganization">
                                                                                    <xsl:for-each select="OrganizationContactPerson">
                                                                                        <xsl:for-each select="Name">
                                                                                            <xsl:for-each select="LastName">
                                                                                                <fo:inline font-size="10pt" font-weight="bold">
                                                                                                    <xsl:apply-templates />
                                                                                                </fo:inline>
                                                                                            </xsl:for-each>
                                                                                        </xsl:for-each>
                                                                                    </xsl:for-each>
                                                                                </xsl:for-each>
                                                                            </xsl:for-each>
                                                                        </xsl:for-each>
                                                                    </fo:block>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                            <fo:table-row>
                                                                <fo:table-cell width="64pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>Title</fo:block>
                                                                </fo:table-cell>
                                                                <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>
                                                                        <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                                            <xsl:for-each select="n1:ResearchCoverPage">
                                                                                <xsl:for-each select="ApplicantOrganization">
                                                                                    <xsl:for-each select="OrganizationContactPerson">
                                                                                        <xsl:for-each select="PositionTitle">
                                                                                            <fo:inline font-size="10pt" font-weight="bold">
                                                                                                <xsl:apply-templates />
                                                                                            </fo:inline>
                                                                                        </xsl:for-each>
                                                                                    </xsl:for-each>
                                                                                </xsl:for-each>
                                                                            </xsl:for-each>
                                                                        </xsl:for-each>
                                                                    </fo:block>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                            <fo:table-row>
                                                                <fo:table-cell width="64pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>Address</fo:block>
                                                                </fo:table-cell>
                                                                <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>
                                                                        <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                                            <xsl:for-each select="n1:ResearchCoverPage">
                                                                                <xsl:for-each select="ApplicantOrganization">
                                                                                    <xsl:for-each select="OrganizationContactPerson">
                                                                                        <xsl:for-each select="ContactInformation">
                                                                                            <xsl:for-each select="PostalAddress">
                                                                                                <xsl:for-each select="Street">
                                                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                                                        <xsl:apply-templates />
                                                                                                    </fo:inline>
                                                                                                </xsl:for-each>
                                                                                            </xsl:for-each>
                                                                                        </xsl:for-each>
                                                                                    </xsl:for-each>
                                                                                </xsl:for-each>
                                                                            </xsl:for-each>
                                                                        </xsl:for-each>
                                                                    </fo:block>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                            <fo:table-row>
                                                                <fo:table-cell width="64pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block />
                                                                </fo:table-cell>
                                                                <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="white">
                                                                    <fo:block>
                                                                        <xsl:for-each select="n1:ResearchAndRelatedProject">
                                                                            <xsl:for-each select="n1:ResearchCoverPage">
                                                                                <xsl:for-each select="ApplicantOrganization">
                                                                                    <xsl:for-each select="OrganizationContactPerson">
                                                                                        <xsl:for-each select="ContactInformation">
                                                                                            <xsl:for-each select="PostalAddress">
                                                                                                <xsl:for-each select="State">
                                                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                                                        <xsl:apply-templates />
                                                                                                    </fo:inline>
                                                                                                </xsl:for-each>, <xsl:for-each select="City">
                                                                                                    <fo:inline font-size="10pt" font-weight="bold">
                                                                                                        <xsl:apply-templates />
                                                                                                    </fo:inline>
                                                                                                </xsl:for-each>&#160;</xsl:for-each>
                                                                                        </xsl:for-each>
                                                                                    </xsl:for-each>
                                                                                </xsl:for-each>
                                                                            </xsl:for-each>
                                                                        </xsl:for-each>
                                                                    </fo:block>
                                                                </fo:table-cell>
                                                            </fo:table-row>
                                                        </fo:table-body>
                                                    </fo:table>&#160;&#160;&#160; </fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="367pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>13. OFFICIAL SIGNING FOR APPLICANT ORGANIZATION</fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" text-align="left" width="222pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>14. <fo:inline font-size="inherited-property-value(&apos;font-size&apos;) - 2pt">PRINCIPAL INVESTIGATOR/PROGRAM DIRECTOR ASSURANCE: Icertify that the statements herein are ture, complete and acurate</fo:inline>
                                                </fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="367pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>SIGNATURE OF PI/PD NAMED IN 3a.</fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell display-align="before" number-columns-spanned="2" text-align="left" width="222pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>15.</fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="before" number-columns-spanned="4" width="367pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                        <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                <fo:block>SIGNATURE OF OFFICIAL NAMED IN 13.</fo:block>
                                            </fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
