package net.dreamlu.event.plugin.jam;

import com.intellij.jam.JamElement;
import com.intellij.jam.JamService;
import com.intellij.jam.reflect.JamMethodMeta;
import com.intellij.psi.PsiMethod;
import com.intellij.semantic.SemKey;

/**
 * event 监听器
 *
 * @author L.cm
 */
public class JamEventListener implements JamElement {
	public static final SemKey<JamEventListener> LISTENER_JAM_KEY = JamService.JAM_ELEMENT_KEY.subKey("EventListener");
	public static final JamMethodMeta<JamEventListener> METHOD_META = new JamMethodMeta(null, JamEventListener.class, LISTENER_JAM_KEY);

	private final PsiMethod psiMethod;

	public JamEventListener(PsiMethod psiMethod) {
		this.psiMethod = psiMethod;
	}

	public PsiMethod getPsiMethod() {
		return psiMethod;
	}

}
