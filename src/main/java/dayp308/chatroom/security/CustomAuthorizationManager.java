package dayp308.chatroom.security;

import dayp308.chatroom.service.CategoryMemberService;
import dayp308.chatroom.service.PostService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

// https://stackoverflow.com/questions/77236793/custom-method-in-preauthorize
@Component("customAuthorizationManager")
public class CustomAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final CategoryMemberService categoryMemberService;
    private final PostService postService;

//    @Autowired
    public CustomAuthorizationManager(CategoryMemberService categoryMemberService, PostService postService) {
        this.categoryMemberService = categoryMemberService;
        this.postService = postService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation invocation) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(invocation.getMethod().getAnnotation(PreAuthorize.class).value());
        CustomMethodSecurityExpressionHandler handler = new CustomMethodSecurityExpressionHandler(categoryMemberService, postService);
        EvaluationContext ctx = handler.createEvaluationContext(authentication, invocation);
        boolean granted = Boolean.TRUE.equals(expression.getValue(ctx, Boolean.class));
        return new AuthorizationDecision(granted);
    }
}
