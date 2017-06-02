import org.junit.Assert;
import org.junit.Test;
import us.blockbox.uilib.WordWrapper;

import java.util.List;
import java.util.regex.Pattern;

public class WordWrapperTest{
	@Test
	public void testWrapper(){
		Pattern spaces = Pattern.compile(" ");
		String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec maximus fringilla diam, sed vehicula erat luctus eget. Proin lobortis, nulla a lobortis scelerisque, nunc ex vulputate libero, quis lacinia sem leo id justo. Etiam sodales metus eget nisl viverra tincidunt. Nullam non lacus tincidunt, gravida metus eget, vestibulum nisi. Praesent.";
		WordWrapper w = new WordWrapper(s);
		int lineMaxLength = 50;
		for(int i = -1;i < 2;i++){
			int lineMaxLength1 = lineMaxLength + i;
			List<String> wrap = w.wrap(lineMaxLength1);
			StringBuilder sb = new StringBuilder(s.length());
			for(String s1 : wrap){
//			System.out.print(s1);
//			System.out.println(s1.length());
				Assert.assertTrue(s1.length() <= lineMaxLength1);
				sb.append(s1);
			}
			String reconstructed = spaces.matcher(sb.toString()).replaceAll("");//todo
			Assert.assertEquals(spaces.matcher(s).replaceAll(""),reconstructed);
		}

	}
}
