package dayp308.chatroom.security;

import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

// https://stackoverflow.com/questions/77236793/custom-method-in-preauthorize
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final CategoryMemberService categoryMemberService;
    private final PostService postService;

    public CustomMethodSecurityExpressionHandler(CategoryMemberService categoryMemberService, PostService postService) {
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        StandardEvaluationContext ctx = (StandardEvaluationContext) super.createEvaluationContext(authentication, mi);
        MethodSecurityExpressionOperations op = (MethodSecurityExpressionOperations) ctx.getRootObject().getValue();
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, this.categoryMemberService, this.postService);
        ctx.setRootObject(root);
        return ctx;
    }
}
