import cmp.mgmt.CmpBase;
import rda.Items.Item32;

public class TestThread extends CmpBase {

	Item32 val1 = new Item32(0x1234);
	Item32 val2 = new Item32(0x1235);
	
	public TestThread()
	{
		super(0x100);

		val1.set(0xDEADBEEF);
		val2.set(0x1CEC001);
	}
	
	
	@Override
	public void run()
	{
		while ( isRunning() )
		{
			System.out.println(String.format("Val1: 0x%08X", val1.get()) );
			System.out.println(String.format("Val2: 0x%08X", val2.get()) );

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
