e\="*">\r\n<editor id\="org.eclipse.jdt.debug.ui.SnippetEditor"/>\r\n</info>\r\n<info extension\="shtml" name\="*">\r\n<editor id\="org.eclipse.ui.browser.editorSupport"/>\r\n</info>\r\n<info extension\="xsl" name\="*">\r\n<editor id\="org.eclipse.wst.xsl.ui.XSLEditor"/>\r\n</info>\r\n<info extension\="ddl" name\="*">\r\n<editor id\="org.eclipse.datatools.sqltools.sqlscrapbook.SQLScrapbookEditor"/>\r\n<defaultEditor id\="org.eclipse.datatools.sqltools.sqlscrapbook.SQLScrapbookEditor"/>\r\n</info>\r\n<info extension\="jspf" name\="*">\r\n<editor id\="org.eclipse.jst.jsp.core.jspsource.source"/>\r\n</info>\r\n<info extension\="tagf" name\="*">\r\n<editor id\="org.eclipse.jst.jsp.core.jspsource.source"/>\r\n</info>\r\n<info extension\="htm" name\="*">\r\n<editor id\="org.eclipse.jst.pagedesigner.PageDesignerEditor"/>\r\n<editor id\="org.eclipse.ui.browser.editorSupport"/>\r\n</info>\r\n<info extension\="html" name\="*">\r\n<editor id\="org.eclipse.jst.pagedesigner.PageDesignerEditor"/>\r\n<editor id\="org.eclipse.ui.browser.editorSupport"/>\r\n</info>\r\n<info extension\="jsf" name\="*">\r\n<editor id\="org.eclipse.jst.jsp.core.jspsource.source"/>\r\n<editor id\="org.eclipse.jst.pagedesigner.PageDesignerEditor"/>\r\n</info>\r\n<info extension\="jardesc" name\="*">\r\n<editor id\="org.eclipse.jdt.ui.JARDescEditor"/>\r\n<defaultEditor id\="org.eclipse.jdt.ui.JARDescEditor"/>\r\n</info>\r\n<info extension\="js" name\="*">\r\n<editor id\="org.eclipse.wst.jsdt.ui.CompilationUnitEditor"/>\r\n<defaultEditor id\="org.eclipse.wst.jsdt.ui.CompilationUnitEditor"/>\r\n</info>\r\n<info extension\="server" name\="*">\r\n<editor id\="org.eclipse.wst.server.ui.editor"/>\r\n</info>\r\n<info extension\="sqlpage" name\="*">\r\n<editor id\="org.eclipse.datatools.sqltools.sqlscrapbook.SQLScrapbookEditor"/>\r\n<defaultEditor id\="org.eclipse.datatools.sqltools.sqlscrapbook.SQLScrapbookEditor"/>\r\n</info>\r\n</editors>
                                                                                                                       allThroughState/0    O Y setIns/1€€€у union/1€€юб 
getValue/0   
  1 j ƒ ∆ ћ № €#( part/1€€юй createRotateRight/2€€€ infoString/1€€юў getFalseExpression/0€€€ mk_tokenSet_4/0    	setKind/1€€€g mSEMI/1€€юм getTrueExpression/0€€€ 
getPHdrs/0€€€5 equals/1   3 / 1 8 9 ; > D F J M O S X Z ^ ` d f g i j r { С Ь ƒ ∆ ћ ж з й к л м н п с т х ц ч ш ъ ю#%&), clearCovering/1€€€« getLeftKey/0    1#( readDDWORD/0€€юд getSymbolFinder/0    ї њ ј Ѕ √ » ÷ „ 
register/1    : = A C G H K Q R V W Y ] a b e h getDefaultValue/0€€€в useUnicode/0€€юг allBitsOne/1€€€ getConnector/0€€€ю coveringRegisters/1€€€ set/3€€€ў getTraceInfo/1€€€щ expr/0€€юл getStateMap/0€€€ъ getNondet8Prototype/0€€юс 
getValue/1    F O d g getParent/1€€€џ isDLLFile/0€€€0 getRVA/0€€€& getDataType/2€€€O log/1€€юў createOperation/2€€€ 
entrySet/0€€юЏ isReadOnly/1    ї ј » ÷ „ parse_level5/0€€€ж getVerbosity/0€€юф 
mCOMMENT/1€€юм predicateIterator/0€€€• getRemainingWorks/0€€€ъ getLength/0    С instr_name_tail/0€€юл 	getName/1€€€д getFormulas/0€€€л mFNEG/1€€юм getRegisters/0    
multiply/1    $ D J S л mUNDERSCORE/1€€юм getCoveredStates/1€€€џ post/2    4 a mGTU/1€€юм 	getType/0        Ћ ъ mLTU/1€€юм applyTemplates/1€€€ getSegmentRegister/1€€€k setAssemblyMap/1€€€л countIndirectBranches/0€€€я getRTLEquivalent/2€€юу setBytesTop/3€€€ѕ makeBVExtract/3€€юф getAssumption/0€€€	 createFalse/0    ) E L N T isElfHeader/1€€€5 record/1€€€Ё getBranchDestination/0    n А Б Й У getDestination/0     э € getDebugLevel/0€€юў setCondition/1€€€ь getAssignments/0€€€" createTrue/0    ) E L N T loadSymbolsBySection/1€€€5 createShiftLeft/2€€€ setVerbosity/1€€юф getMoveDestination/0    w Н 	getZero/0    ≈ « Ќ getChildren/1€€€џ post/3    - 4 : = A C G H K Q R V W Y ] b e h row/0€€€ж getBaseFileName/1€€€б normalizeAssignments/0€€юь 	mRPAREN/1€€юм 	mLPAREN/1€€юм 
setState/1€€€Ћ getMaxAddress/0    ї ј » ќ isDebugEnabled/0€€юў getConversionCount/0€€юб getAddressFactory/0package org.analysis.cfg;

import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.util.List;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLParser {

	CFG cfg = new CFG(null);

	public XMLParser() {

	}

	public XMLParser(CFG cfg) {
		this.cfg = cfg;
	}

	public void WriteXMLFile() {
		try {
			Document doc = new Document();
			Element root = new Element("CFG");
			doc.setRootElement(root);

			// root
			Element vertexs = new Element("Vertexs");
			root.addContent(vertexs);

			// root/Vertexs
			for (CFGVertex vertex : cfg.vertices.vertices) {
				// CFGVertex
				Element cfgvertex = new Element("CFGvertex");
				cfgvertex.setAttribute(new Attribute("Type", String.valueOf(vertex.getType()[0])));
				cfgvertex.setAttribute(new Attribute("Address", String.valueOf(vertex.getAddr()
						.getValue()), Namespace.getNamespace("xsd",String.valueOf(vertex.getAddr().getValue()))));
				vertexs.addContent(cfgvertex);

				// CFGVertex/Ins
				Element ins = new Element("Ins");
				ins.addContent(new Element("Content").setText("cmp eax, 0"));
				ins.addContent(new Element("Operand").setText("+"));
				ins.addContent(new Element("Operator").setText("a"));
				ins.addContent(new Element("Value").setText("string"));
				cfgvertex.addContent(ins);

				// CFGVertex/EdgeOut
				Element edgeout = new Element("EdgeOut");
				// for (CFGEdge edge : vertex.out) {
				// // CFGEdge
				// Element cfgedge = new Element("CFGEdge");
				// cfgedge.setAttribute(new Attribute("Destination",
				// String.valueOf(edge.getDestination().getAddr().getValue())));
				// edgeout.addContent(cfgedge);
				//
				// // CFGEdge/SymbolicExecution
				// Element se = new Element("SymbolicExecution");
				// se.addContent(new Element("string").setText("a &gt;= 0"));
				// cfgedge.addContent(se);
				// }
				cfgvertex.addContent(edgeout);
			}

			// root/SymbolValues
			Element symbolvalues = new Element("SymbolValues");
			root.addContent(symbolvalues);

			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("f:\\cfg.xml"));

			System.out.println("File Saved!");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}

	public void ReadXMLFile() {

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("c:\\file.xml");

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("staff");

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);

				System.out.println("First Name : " + node.getChildText("firstname"));
				System.out.println("Last Name : " + node.getChildText("lastname"));
				System.out.println("Nick Name : " + node.getChildText("nickname"));
				System.out.println("Salary : " + node.getChildText("salary"));

			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	public void ModifyXMLFile() {

		try {

			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File("c:\\file.xml");

			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();

			// update staff id attribute
			Element staff = rootNode.getChild("staff");
			staff.getAttribute("id").setValue("2");

			// add new age element
			Element age = new Element("age").setText("28");
			staff.addContent(age);

			// update salary value
			staff.getChild("salary").setText("7000");

			// remove firstname element
			staff.removeChild("firstname");

			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("c:\\file.xml"));

			// xmlOutput.output(doc, System.out);

			System.out.println("File updated!");
		} catch (IOException io) {
			io.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

}
                                                                                                           eBVGreaterThan/2€€юф createRotateLeftWithCarry/2€€€ 
mRSHIFTA/1€€юм getProgram/0€€€я containsKey/2   #%( error/2€€юў 
putDWORD/1€€€1 getAssignment/1€€€" getReturnStatus/0€€€ш getAddress/0     Ь Љ — й с 	compare/2       3 Ћ 	verbose/0€€юў 	isMinus/1€€€ж create/2    ) isCompleted/0    + . mk_tokenS