package proxy;

import junit.framework.TestCase;

import org.junit.Test;

public class SlimProxyTest extends TestCase {

	private final String sampleInstruction = "[000012:000085:[000004:000017:decisionTable_0_0:000004:make:000015:decisionTable_0:000008:Division:]:000264:[000005:000017:decisionTable_0_1:000004:call:000015:decisionTable_0:000005:table:000174:[000003:000062:[000003:000009:numerator:000011:denominator:000009:quotient?:]:000037:[000003:000002:10:000001:5:000001:2:]:000042:[000003:000004:2000:000002:20:000003:100:]:]:]:000082:[000004:000017:decisionTable_0_2:000004:call:000015:decisionTable_0:000005:reset:]:000099:[000005:000017:decisionTable_0_3:000004:call:000015:decisionTable_0:000012:setNumerator:000002:10:]:000100:[000005:000017:decisionTable_0_4:000004:call:000015:decisionTable_0:000014:setDenominator:000001:5:]:000084:[000004:000017:decisionTable_0_5:000004:call:000015:decisionTable_0:000007:execute:]:000085:[000004:000017:decisionTable_0_6:000004:call:000015:decisionTable_0:000008:quotient:]:000082:[000004:000017:decisionTable_0_7:000004:call:000015:decisionTable_0:000005:reset:]:000101:[000005:000017:decisionTable_0_8:000004:call:000015:decisionTable_0:000012:setNumerator:000004:2000:]:000101:[000005:000017:decisionTable_0_9:000004:call:000015:decisionTable_0:000014:setDenominator:000002:20:]:000085:[000004:000018:decisionTable_0_10:000004:call:000015:decisionTable_0:000007:execute:]:000086:[000004:000018:decisionTable_0_11:000004:call:000015:decisionTable_0:000008:quotient:]:]";
	private final String sampleResponse = "[000012:000044:[000002:000017:decisionTable_0_0:000002:OK:]:000105:[000002:000017:decisionTable_0_1:000063:__EXCEPTION__:message:<<NO_METHOD_IN_CLASS table[1] Division.>>:]:000105:[000002:000017:decisionTable_0_2:000063:__EXCEPTION__:message:<<NO_METHOD_IN_CLASS reset[0] Division.>>:]:000044:[000002:000017:decisionTable_0_3:000002:10:]:000043:[000002:000017:decisionTable_0_4:000001:5:]:000107:[000002:000017:decisionTable_0_5:000065:__EXCEPTION__:message:<<NO_METHOD_IN_CLASS execute[0] Division.>>:]:000043:[000002:000017:decisionTable_0_6:000001:2:]:000105:[000002:000017:decisionTable_0_7:000063:__EXCEPTION__:message:<<NO_METHOD_IN_CLASS reset[0] Division.>>:]:000046:[000002:000017:decisionTable_0_8:000004:2000:]:000044:[000002:000017:decisionTable_0_9:000002:20:]:000108:[000002:000018:decisionTable_0_10:000065:__EXCEPTION__:message:<<NO_METHOD_IN_CLASS execute[0] Division.>>:]:000046:[000002:000018:decisionTable_0_11:000003:100:]:]";
	private final String sampleLaunchCommand = "C:\\Ruby19\\bin\\ruby.exe -Ku  -I  C:\\fixtures\\ C:\\RubySlim\\lib\\run_ruby_slim.rb";
	private SlimProxyClient slimPClient;
	private Process p;

	public void setUp() throws Exception {
		p = Runtime.getRuntime().exec(sampleLaunchCommand + " 8085");
		Thread.sleep(1000);
		slimPClient= new SlimProxyClient("127.0.0.1", 8085);
		slimPClient.connect();
	}

	public void tearDown() throws Exception {
		slimPClient.sendBye();
		Thread.sleep(1000);
		p.destroy();
	}

	@Test
	public void testGetSlimProtocolVersion() throws Exception {
		setUp();
		assertEquals("Slim -- V0.1", slimPClient.getSlimProtocolVersion());
		tearDown();
	}

	@Test
	public void testSendSampleInstructionRubySlim() throws Exception {
		setUp();
		assertEquals(sampleResponse, slimPClient.invokeAndGetResponse(sampleInstruction));
		tearDown();
	}
}
