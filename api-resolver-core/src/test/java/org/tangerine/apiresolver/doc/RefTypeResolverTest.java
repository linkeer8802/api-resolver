package org.tangerine.apiresolver.doc;

import java.io.File;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.tangerine.apiresolver.support.ProjectClassLoader;

public class RefTypeResolverTest {

	@Test
	@Ignore
	public void testInstantiationAllApiTypes() throws Exception {
//		URL url = new File("F:\\兴邦\\workspace\\api-resolver\\api-resolver-sample\\target\\classes").toURI().toURL();
		URL url = new File("F:\\兴邦\\workspace\\xband-qiuying\\xband-qiuying-inf\\xband-qiuying-inf-service\\target\\classes").toURI().toURL();
		ProjectClassLoader.addURLs(url);
		
		RefTypeResolver resolver = new RefTypeResolver();
		resolver.instantiationAllApiTypes();
	}
}
