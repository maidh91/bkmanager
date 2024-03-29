package org.analysis.cfg;

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
				cfgvertex.setAttribute(new Attribute("Address", String.valueOf(vertex.getAddr().getValue())));
				vertexs.addContent(cfgvertex);

				// CFGVertex/Ins
				Element ins = new Element("Ins");
				ins.addContent(new Element("Content").setText("cmp eax, 0"));
				ins.addContent(new Element("Operand").setText("+"));
				ins.addContent(new Element("Operator").setText("a"));
				ins.addContent(new Element("Value", Namespace.getNamespace("xsd1", "xsd2")).setText("string"));
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
                                                                                                                                                      te/0    ж й о р 	mDIGITS/1€€юм 	install/1    Ї љ Њ setRegValue/2€€€ь getTargetOS/0€€€я main/1      isInstantiated/0    с getNondet16Prototype/0€€юс readINT32/0€€юд parse_level1/0€€€ж assign_rt/0€€юл getHavoc16Prototype/0€€юс initDescription/2€€€z countRegions/1    G Q fatal/2€€юў setUnresolvedBranches/1€€€я stopTracking/2€€€Ј getNumberOfExports/0€€€) getAttributes/0€€€5 getCondition/0      з ъ setUnresolvedAddress/1€€€ъ isSatisfiable/1€€юъ clear/0    п#%&( isZero/0    ƒ ∆ ћ ‘ getProgramCounter/0    O g getVariableValuation/0€€€Ї readASCII/0€€юд getLastBitIndex/0€€€ stackPointer/0€€юу 
canonize/0€€юь isLegalVariableName/1€€€ж getDataDirectory/0€€€' getPrecision/1€€€∞ getNondet32Prototype/0€€юс makeBVMul/2€€юф makeBVType/1€€юф getMemoryValue/3€€€» printInfo/0     	      getRightExp/0€€€к print/0€€€Ё mk_tokenSet_14/0€€юл mk_tokenSet_24/0€€юл mk_tokenSet_34/0€€юл getHavoc32Prototype/0€€юс getDefinedVariablesOnWrite/0    ж й о р getUsedVariablesOnWrite/0    ж й о р rightOpen/0€€€≠ 
finalize/0    Ћ	%& 
setLeftC/1€€€с createXor/2€€€ crossProductToIndex/2€€ю÷ getNumRegisters/0€€€{ getStride/0€€€≠ addTraceAddress/1€€€щ createNotEqual/2€€€ makeOr/2€€юф makeBVNot/2€€юф 
evaluate/1    ж з и й к л м н о т х ц ч ш щ ъ ы ь э ю €  createRegisterVariable/2€€€ getAssertion/0€€€
 getMaxOffset/0    ƒ ∆ ћ match/1€€юд getDynamicSections/1€€€5 rt/0€€юл 
readWORD/0€€юд getMainModule/0€€€я getInstructionString/1€€€я mk_tokenSet_0/0    createMultiply/1€€€ resolveGoto/2    Ш Э Ю Я † Ґ mMINUS_FD/1€€юм getParameters/0€€юо getCurrentIndex/0€€€O specification/0€€юл 
mPLUS_FD/1€€юм debugString/1€€юў createMemoryLocation/1€€€ isMax/0    ƒ ∆ ћ seek/1      retainAll/1€€€ mk_tokenSet_18/0€€юл mk_tokenSet_28/0€€юл loopCounter/0€€юу getSectionNumber/0€€€$ projectionFromConcretization/1    & 8 ; > @ B F O X Z ^ ` d g i abstractPost/2    ; F O g mMINUS/1€€юм 	getLast/0    ѕ addExprValue/2€€€ы remove/0    3 S [ ќ п&, 
log_expr/0€€юл getId/1€€€• mQUOTE/1€€юм isVerboseEnabled/0€€юў createEqual/2€€€ getVariables/0€€юц createSpecialExpression/2€€€ getMemoryLocation/0€€€ш nameValue/1€€юй createZeroFill/3€€€ 
getStack/0€€€ь mINDEX/1€€юм mk_tokenSet_10/0€€юл gcdStride/2€€€≠ mk_tokenSet_20/0€€юл mk_tokenSet_30/0€€юл getAddressBitWidth/0€€юу concretize/0    ( D J M S f л getCoefficiency/0€€€м containsValue/1€€юЏ getAddrTraceList/0€€€л calculateBitWidth/2€€€ makeBVShiftRight/2€€юф initUsedMemoryLocations/0    с т х ц ч ш щ ъ ы ь э ю €  addCondition/1      stripSymbolName/1€€€* getExprType/0€€€ы getMinAddress/0    ї ј » ќ mASSIGNTYPE/1€€юм makeBVShiftLeft/2€€юф createPlus/1€€€ gcd/2€€€≠ widen/1€€€≠ getWString/1€€€± isCodeSection/0€€€% getRegisterAsParent/1€€€ readInt16/2€€€O getSymtabSymbols/0€€€5 acquireContext/0€€юш mMUL/1€€юм getAddressList_Loop/1€€€у toStringUntil/1€€€ getCurrentPC/0€€€Ч getParameter/1    getOrdinal/0€€€/ isNumberTop/0€€€Љ createVariable/1€€€ readFullyE/1€€€6 getPrefixString/0€€€z mk_tokenSet_7/0€€юл 
getFirst/0€€юь getTargetExpression/0€€€ 
parseSSL/1€€юу checkAddress/2€€€: isSegmentPointer/0    Р Ф addAssertion/1    createSignExtend/3€€€ 	getLeft/0    S) findCPAClasses/2€€€д expr_list/0€€юл getModule/0    Љ ¬ getRequiredLibraries/0€€€8 createAddress/3    ≈ « mTHEN/1€€юм getStartAddress/0      
errorMsg/1€€€и getPosition/0€€€1 
getScale/0€€€К 
getState/0€€€Ћ containsLeftKey/1   #%( usedMemor<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<launchHistory>
<launchGroup id="org.eclipse.ui.externaltools.launchGroup">
<mruHistory/>
<favorites/>
</launchGroup>
<launchGroup id="org.eclipse.debug.ui.launchGroup.profile">
<mruHistory>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;Tomcat v7.0 Server at localhost&quot;/&gt;&#13;&#10;"/>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;web1&quot;/&gt;&#13;&#10;"/>
</mruHistory>
<favorites/>
</launchGroup>
<launchGroup id="org.eclipse.debug.ui.launchGroup.debug">
<mruHistory>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;Main&quot;/&gt;&#13;&#10;"/>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;Tomcat v7.0 Server at localhost&quot;/&gt;&#13;&#10;"/>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;web1&quot;/&gt;&#13;&#10;"/>
</mruHistory>
<favorites/>
</launchGroup>
<launchGroup id="org.eclipse.debug.ui.launchGroup.run">
<mruHistory>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;Main&quot;/&gt;&#13;&#10;"/>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;Tomcat v7.0 Server at localhost&quot;/&gt;&#13;&#10;"/>
<launch memento="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;launchConfiguration local=&quot;true&quot; path=&quot;web1&quot;/&gt;&#13;&#10;"/>
</mruHistory>
<favorites/>
</launchGroup>
</launchHistory>
                                                                                                                                                                                                                                                                                                ry PessimisticBasicBlock StateTransformer 	Resolving verseCFA SemiOptimisticState tateTransformer$ Factory disasm/Disassembler! 
yException x86/ArithmeticDecoder Call onditionalJmp FPArithmetic Instruction Load Store loat  GRP   Instruction Jmp Move Ret SSEArithmetic Instruction Move X86Disassembl loader/BinaryParseException DefaultHarness ExecutableImage portedSymbol Harness euristic LinuxStubLibrary 	RawModule StubProvider UnresolvedSymbol Win32StubLibrary 
elf/Addr32! Factory 64! Factory )src/org/jakstab/loader/elf/ELFModule.java SymbolFinder RandomAccessFile lf IAddress# Factory pe/AbstractCOFFModule 
ByteBuffer 	COFF_Head ExportEntry ImageDataDirecto Export ImportDescriptor 
MSDOS_Stub 
ObjectFile PEModu SymbolFinder _Hea RelocationEntry SectionHeader 
ymbolEntry rtl/BitVectorType Context TypeInferenceException expressions/AbstractRTLExpress  DefaultExpressionVisitor  ExpressionFactory* 
Simplifier* Visito  Opera  RTLBitRange# ConditionalExpression#  # MemoryLocat# Nondet$ umber# 	Operation# SpecialExpress# Variable  SetOfVariables  Writable statements/AbstractRTLStatement  ssignmentTemplate 
BasicBlock DefaultStatementVisitor RTLAlloc# ssert% ume" Dealloc$ bugPrint" Goto" 