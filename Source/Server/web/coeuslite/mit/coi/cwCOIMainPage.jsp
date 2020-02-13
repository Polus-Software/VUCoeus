<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<script src="<%=path%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<script>
            var lastPopup;
            function showPopUp(linkId, frameId, width, height, src, bookmarkId) {
                showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }

            function gotolocation(location){
                window.location=location;
            }
        </script>
</head>
<body>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<tr valign="top">
			<td><table width="100%" border="0" cellpadding="5"
					cellspacing="0">

					<tr>
						<td>
							<table class="tabtable" width="100%" border="0" cellpadding="2"
								cellspacing="0">
								<tr>
									<td class="copy">
										<p>In order to ensure objectivity in research, federal
											regulations have been put in place to uncover the potential
											for real or perceived conflicts of interest which might
											reasonably appear to be affected by an investigator's
											reasearch.</p>
										<p>These regulations require all Principal Investigators
											and others designated as key personnel in proposals to NIH
											and NSF complete a proposal-specific financial conflict of
											interest disclosure pror to the submission of a proposal.</p>
										<p>Completion of an Annual Disclosure by those individuals
											with active awards or pending proposals from NIH and NSF is
											also required.</p> Other federal or non-federal sponsors may also
										require disclosures at the proposal or award stage.
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table class="tabtable" width="100%" border="0" cellpadding="2"
								cellspacing="0">
								<tr>
									<td class="copy"><input type="button"
										value="Add Financial Entities"
										onclick="javascript:gotolocation('<%=path%>/getAddFinEnt.do')"
										style="width: 300px"></td>
								</tr>
								<tr>
									<td class="copy"><input type="button"
										value="Update my Financial Entities"
										onclick="javascript:gotolocation('<%=path%>/getReviewFinEnt.do')"
										style="width: 300px"></td>
								</tr>
								<tr>
									<td class="copy"><input type="button"
										value="Prepare and Submit Project Disclosure"
										onclick="javascript:gotolocation('<%=path%>/getAddDisclosure.do')"
										style="width: 300px"></td>
								</tr>
								<tr>
									<td class="copy"><input type="button"
										value="Review my Current Disclosures"
										onclick="javascript:gotolocation('<%=path%>/getReviewDiscl.do')"
										style="width: 300px"></td>
								</tr>
								<tr>
									<td class="copy">
										<%
        String disabled="disabled";
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute("person");
        if (personInfoBean != null) {
            String personId = personInfoBean.getPersonID();
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            boolean hasPendingDisc = userDetailsBean.hasPendingDisclosure(personId);
            if (hasPendingDisc) {
                disabled="";
                }
            }
                        %> <input type="button"
										value="Prepare and Submit Annual Disclosure"
										onclick="javascript:gotolocation('<%=path%>/getAnnDiscPendingFEs.do')"
										<%=disabled%> style="width: 300px">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table></td>
		</tr>

	</table>

	<!-- Balloon Info - START -->
	<div id="investigators"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onclick="hideBalloon('investigators')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td></td>
					<td align="right"><a
						href="javascript:hideBalloon('investigators')"><img border="0"
							src="<%=path%>/coeusliteimages/none.gif"></a></td>
				</tr>
				<tr>
					<td colspan="2">The term, Investigator, refers to the
						Principal Investigator, Co-Principal Investigator, and any other
						person at the Institute who is responsible for the design,
						conduct, or reporting of research or educational activities funded
						or proposed for funding by NSF or PHS. MIT does not include
						graduate students or persons with postdoctoral appointments in the
						definition of Investigator, unless such individuals are listed in
						the proposal as co-Investigator(s).</td>
				</tr>
			</table>
		</div>
	</div>

	<div id="sigFinInt"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onclick="hideBalloon('sigFinInt')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td></td>
					<td align="right"><a
						href="javascript:hideBalloon('sigFinInt')"><img border="0"
							src="<%=path%>/coeusliteimages/none.gif"></a></td>
				</tr>
				<tr>
					<td colspan="2">
						<p>The term, Significant financial interest, refers to:</p>

						<p>
							A company or public or non-profit organization from which you
							have received in the last 12 months, or expect to receive in the
							next 12 months, any financial remuneration <b>exceeding
								$10,000 </b> (aggregate for yourself, your spouse, and your
							dependent children) for any of the following:
						</p>
						<ul>
							<li>Salary, director's fees, consulting payments, honoraria,
								royalties</li>
							<li>Payments for patents, copyrights or other intellectual
								property</li>
							<li>Other financial compensation</li>
						</ul>
						<p>
							<b>OR</b>
						</p>
						<p>
							A any company in which you have significant monetary
							interests/value either in <b>excess of $10,000 </b> in fair
							market value or in <b>excess of 5 percent ownership</b>
							(aggregate yourself, your spouse, and your dependent children)
							including any of the following:
						</p>
						<ul>
							<li>Stock or stock options</li>
							<li>Other interests of significant monetary value</li>
						</ul>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div id="exepList"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onclick="hideBalloon('exepList')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td></td>
					<td align="right"><a href="javascript:hideBalloon('exepList')"><img
							border="0" src="<%=path%>/coeusliteimages/none.gif"></a></td>
				</tr>
				<tr>
					<td colspan="2">
						<p>You do not have to disclose as a COI entity any of the
							following:</p>
						<ul>
							<li>Payments from MIT for yourself or immediate family</li>
							<li>Payments from public or non-profit organizations if they
								are for lectures, seminars, or service on advisory committee</li>
							<li>Financial interests in a company that are the result of
								you holding shares in a mutual fund, being a member of the MIT
								retirement plan, or any similar pooled fund in which you do not
								control the selection of investments.</li>
						</ul>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div id="potPerCOI"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onclick="hideBalloon('potPerCOI')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td></td>
					<td align="right"><a
						href="javascript:hideBalloon('potPerCOI')"><img border="0"
							src="<%=path%>/coeusliteimages/none.gif"></a></td>
				</tr>
				<tr>
					<td colspan="2">
						<p>Following are frequently asked questions regarding conflict
							of interest.
						<p>
						<ol>
							<li><b>What constitutes a conflict of interest in
									research?</b>

								<p>Conflicts of interest arise when there is the potential,
									from an independent observer's perspective, that an
									Investigator's personal, financial or consulting interests may
									influence their decisions, independence or judgment in
									conducting or publishing research. Among the concerns are:
									employment of family or friends, personal relationships within
									work groups, the use of MIT's name in outside activities and
									financial holdings, or interests that may influence their
									professional activities. Particular attention is paid to
									financial conflicts of interest. From the Federal Government's
									point of view, a financial conflict of interest occurs whenever
									an Investigator receives annually $10,000+ in income from, or
									holdings in, companies that could benefit financially from the
									research.</p></li>

							<li><b>Why is perceived conflict of interest a concern?</b>

								<p>While it may be apparent to the Investigator that the
									research they are doing on campus is sufficiently different
									from their activities producing financial gain, it is not
									always so clear to the outside observer. In such cases, it is
									important to be able to articulate and maintain the distinction
									and separation of MIT research activities from outside
									professional activities. Our conflict of interest management
									group enables researchers to clarify and manage these
									distinctions so that there is no confusion among sponsors,
									collaborators or associates.</p></li>

							<li><b>How do I know if there is a potential conflict of
									interest with an entity where I receive income (or hold
									equity)?</b>

								<p>Ask yourself the following questions. If the answer to
									any of them is 'yes', then there is a potential conflict of
									interest that should be discussed with the Office of Sponsored
									Programs or the Vice President for Research.
								<ul>
									<li>Are the results of my research relevant to the
										products or services of the entity in which there is a
										financial interest?</li>
									<li>Could I or the company I consult for benefit
										financially from the research for this proposal/award?</li>
									<li>Are students involved, and are any constraints and
										restrictions being imposed upon their work?</li>
									<li>Is the commercial entity providing any products,
										personnel, or the use of their facility for this research?</li>
									<li>How am I keeping my interests and obligations to the
										entity separate from my MIT teaching and research
										responsibilities?</li>
									<li>If this research results in a patentable invention
										would this entity want to license?</li>
								</ul>
								</p></li>

							<li><b>Are there examples of situations that pose a
									financial conflict of interest in research?</b>

								<p>Here are some situations that present financial conflict
									of interest in research:
								<ul>
									<li>accepting research sponsorship, directly or indirectly
										from a company in which you have significant financial
										interest</li>
									<li>not disclosing personal or family ownership or
										significant financial interests in companies doing business
										with MIT to Department Head or Laboratory or Center Director</li>
									<li>restricting or delaying publication or dissemination
										of the results from MIT research because of a desire to hold
										results for an outside company to develop inventions</li>
								</ul>
								</p></li>
						</ol>
						</p>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div id="rewCOI"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onclick="hideBalloon('rewCOI')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td></td>
					<td align="right"><a href="javascript:hideBalloon('rewCOI')"><img
							border="0" src="<%=path%>/coeusliteimages/none.gif"></a></td>
				</tr>
				<tr>
					<td colspan="2"></td>
				</tr>
			</table>
		</div>
	</div>

	<div id="mitCOI" class="dialog" style="width: 500px; height: 350px">
		<div style="overflow: auto; width: 500px; height: 350px">
			<table border="0" cellpadding="2" cellspacing="0"
				class="lineBorderWhiteBackgrnd">
				<tr>
					<td colspan="2">
						<p>
							Investigators should familiarize themselves with the following
							sections of the MIT Policies and Procedures <a
								href="https://web.mit.edu/policies/" target="_blank">http://web.mit.edu/policies/</a>
							that pertain to potential conflict of interest in research.
						</p>
						<p>MIT Policies and Procedures</p>
						<dl>
							<dt>4.0 Faculty Rights and Responsibilities</dt>
							<dd>
								4.4 Conflict of Interests <a
									href="https://web.mit.edu/policies/4.4.html" target="_blank">http://web.mit.edu/policies/4.4.html</a><br>
								4.5 Outside Professional Activities <a
									href="https://web.mit.edu/policies/4.5.html" target="_blank">http://web.mit.edu/policies/4.5.html</a>
							</dd>
							<dt>12.0 Relations with the Public, Use of MIT Name, and
								Facilities Use</dt>
							<dd>
								12.3 Use of Institute Name <a
									href="https://web.mit.edu/policies/12.3.html" target="_blank">http://web.mit.edu/policies/12.3.html</a><br>
								12.4 Use of Institute Letterhead <a
									href="https://web.mit.edu/policies/12.4.html" target="_blank">http://web.mit.edu/policies/12.4.html</a>
							</dd>
							<dt>13.0 Information Policies</dt>
							<dd>
								13.1 Intellectual Property <a
									href="https://web.mit.edu/policies/13.1.html" target="_blank">http://web.mit.edu/policies/13.1.html</a><br>
								13.1.1 Ownership of Intellectual Property <br> 13.1.2
								Significant Use of MIT - Administered Resources<br> 13.1.3
								Ownership of Copyrights in Theses<br> 13.1.4 Invention and
								Proprietary Information Agreements<br> 13.1.5 Consulting
								Agreements<br> 13.1.7 Disclosures and Technology Transfer
							</dd>
						</dl>

					</td>
				</tr>
			</table>
		</div>
		<p>
			<input type="button" onclick="javascript:hm('mitCOI')" value="Close">
		</p>
	</div>
	<!-- Balloon info - END -->

</body>
</html>
