
use nom::*;
use crate::lexer::*;
use nom::sequence::tuple;
use nom::combinator::map;
use std::rc::Rc;


 use nom::{
  IResult,
  branch::alt,
  combinator::opt,
  multi::{many1, many0},
  bytes::complete::{tag},
  sequence::{preceded},
  character::complete::{alphanumeric1, digit1},
};
 
// Here are the different node types. You will use these to make your parser.
// You may add other nodes as you see fit.

#[derive(Debug, Clone, PartialEq)]
pub enum Node {
  Program { children: Vec<Node> },
  Statement { children: Vec<Node> },
  FunctionDefine {name: Vec<u8>, children: Vec<Node> },
  FunctionArguments { children: Vec<Node> },
  FunctionStatements { children: Vec<Node> },
  LoopStatement {children: Vec<Node> },
  IfExpression {condition: Rc<Node>, if_block: Vec<Node>, else_block: Vec<Node> },
  Expression { children: Vec<Node> },
  MathExpression {name: Vec<u8>, children: Vec<Node> },
  FunctionCall { name: Vec<u8>, children: Vec<Node> },
  VariableDefine { children: Vec<Node> },
  FunctionReturn { children: Vec<Node> },
  Number { value: Vec<u8> },
  Bool { value: bool },
  Identifier { value: Vec<u8> },
  String { value: Vec<u8> },
  Comment { value: Vec<u8> },
  Null,
}

// Some helper functions to use Tokens instead of a &str with Nom. 
// You'll probably have to create more of these as needed.

pub fn t_alpha(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Alpha => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_digit(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Digit => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_true(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::True => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_false(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::False => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_alpha1(input: Tokens) -> IResult<Tokens, Vec<Token>> {
  many1(t_alpha)(input)
}

pub fn t_alpha0(input: Tokens) -> IResult<Tokens, Vec<Token>> {
  many0(t_alpha)(input)
}

pub fn t_alphanumeric1(input: Tokens) -> IResult<Tokens, Vec<Token>> {
  many1(alt((t_alpha,t_digit)))(input)
}

pub fn t_alphanumeric0(input: Tokens) -> IResult<Tokens, Vec<Token>> {
  many0(alt((t_alpha,t_digit,)))(input)

}

// keywords 

pub fn t_left_paren(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::LeftParen => true,
    _=> false,
  }) ;
  fxn(input.clone())
}

pub fn t_right_paren(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::RightParen => true,
    _=> false,
  }) ;
  fxn(input.clone())
}

// Helper function to parse the curly brackets
pub fn t_left_curly(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::LeftCurly => true,
    _=> false,
  }) ;
  fxn(input.clone())
}

pub fn t_right_curly(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::RightCurly => true,
    _=> false,
  }) ;
  fxn(input.clone())
}

pub fn t_vertical_bar(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::VerticalBar => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_double_vertical_bar(input: Tokens) -> IResult<Tokens, Token>{
  let (input, x) = t_vertical_bar(input)?;
  let (input, x) = t_vertical_bar(input)?;
  Ok((input, Token::new()))
}

pub fn t_caret(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Caret => true,
  _ => false,
  });
  fxn(input.clone())
}

pub fn t_quote(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Quote => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_exclamation(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Exclamation => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_slash(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Slash => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_comma(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::Comma => true,
    _=> false,
  }) ;
  fxn(input.clone())
}

pub fn t_ampersand(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Ampersand => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_doubleamper(input: Tokens) -> IResult<Tokens, Token>{
  let (input, x) = t_ampersand(input)?;
  let (input, x) = t_ampersand(input)?;
  Ok((input, Token::new()))
}

pub fn t_semicolon(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(&|tk| match tk.kind {
    TokenKind::Semicolon => true,
    _=> false,
  }) ;
  fxn(input.clone())
}


pub fn t_let(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Let => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_loop(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Loop => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_if(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::If => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_else(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Else => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_fn(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Fn => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_return(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Return => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_whitespace(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::WhiteSpace => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_plus(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Plus => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_star(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Star => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_dash(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Dash => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_greater(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Greater => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_less_than(input: Tokens) -> IResult<Tokens, Token>{
  let fxn = check_token(& |tk| match tk.kind{
    TokenKind::Less => true,
    _ => false,
  });
  fxn(input.clone())
}
pub fn t_equal(input: Tokens) -> IResult<Tokens, Token> {
  let fxn = check_token(& |tk| match tk.kind {
    TokenKind::Equal => true,
    _ => false,
  });
  fxn(input.clone())
}

pub fn t_not_equal(input: Tokens) -> IResult<Tokens, Token>{
  let (input, x) =t_exclamation(input)?;
  let (input, x) =t_equal(input)?;
  Ok((input, Token::new()))
}

pub fn t_double_equal(input: Tokens) -> IResult<Tokens, Token>{
  let(input, x) = t_equal(input)?;
  let(input, x) = t_equal(input)?;
  Ok((input, Token::new()))
}

pub fn t_greater_equal(input: Tokens) -> IResult<Tokens, Token>{
  let(input, x) = t_greater(input)?;
  let(input, x) = t_equal(input)?;
  Ok((input, Token::new()))
}

pub fn t_less_equal(input: Tokens) -> IResult<Tokens, Token>{
  let(input, x) = t_less_than(input)?;
  let(input, x) = t_equal(input)?;
  Ok((input, Token::new()))
}

pub fn l1(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = l2(input)?;
  let(input, x2) = l1_inf(input)?;
  Ok((input, x))
}

pub fn l1_inf(input: Tokens) -> IResult<Tokens, Node> {
  let(input, operator) = alt((t_plus, t_dash))(input)?;
  let(input, expression) = l2(input)?;
  Ok((input, Node::MathExpression{name: operator.lexeme, children: vec![expression]}))
}

pub fn l2(input: Tokens) -> IResult<Tokens, Node> {
  let(input, x) = l3(input)?;
  let(input, x2) = l2_inf(input)?;
  Ok((input, x))
}

pub fn l2_inf(input: Tokens) -> IResult<Tokens, Node>{
  let(input, operator) = alt((t_star, t_slash))(input)?;
  let(input, expression) = l3(input)?;
  Ok((input, Node::MathExpression{name: operator.lexeme, children: vec![expression]}))
}

pub fn l3(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = l4(input)?;
  let(input, x2) = l3_inf(input)?;
  Ok((input, x))
}

pub fn l3_inf(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = t_caret(input)?;
  let(input, expression) = l4(input)?;
  Ok((input, expression))
}

pub fn l4(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = l5(input)?;
  let(input, x2) = l4_inf(input)?;
  Ok((input, x))
}

pub fn l4_inf(input: Tokens) -> IResult<Tokens, Node>{
  let(input, operator) = alt((t_doubleamper, t_double_vertical_bar))(input)?;
  let(input, expression) = l5(input)?;
  Ok((input, Node::MathExpression{name: operator.lexeme, children: vec![expression]}))
}

pub fn l5(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = l6(input)?;
  let(input, x2) = l5_inf(input)?;
  Ok((input, x))
}

pub fn l5_inf(input: Tokens) -> IResult<Tokens, Node>{
  let(input, operator) = alt((t_not_equal, t_double_equal, t_greater_equal, 
        t_greater, t_less_equal, t_less_than))(input)?;
  let(input, expression) = l6(input)?;
  Ok((input, Node::MathExpression{name: operator.lexeme, children: vec![expression]}))
}


pub fn l6(input: Tokens) -> IResult<Tokens, Node>{
  alt((paren_exp, value))(input)
}


pub fn paren_exp(input: Tokens) -> IResult<Tokens, Node>{
  let(input, x) = t_left_paren(input)?;
  let(input, expression) = l1(input)?;
  let(input, x) = t_right_paren(input)?;
  Ok((input, expression))

}

pub fn loop_statement(input: Tokens) -> IResult<Tokens, Node> {
  let(input, x) = t_loop(input)?;
  let(input, x) = t_left_curly(input)?;
  let(input, expression) = l1(input)?;
  let(input, x) = t_right_curly(input)?;
  Ok((input, Node::LoopStatement{children: vec![expression]}))
}

pub fn if_exp(input: Tokens) -> IResult<Tokens, Node> {
  let(input, x) = t_if(input)?;
  let(input, condition) = l1(input)?;
  let(input, x) = t_left_curly(input)?;
  let(input, if_block) = l1(input)?;
  let(input, x) = t_right_curly(input)?;
  let(input, x) = t_else(input)?;
  let(input, x) = t_left_curly(input)?;
  let(input, else_block) = l1(input)?;
  let(input, x) = t_right_curly(input)?;
  Ok((input, Node::IfExpression{condition: Rc::new(condition), if_block: vec![if_block], else_block: vec![else_block]}))
}

pub fn identifier(input: Tokens) -> IResult<Tokens, Node> {
  let (input, first) = t_alpha(input)?;
  let (input, rest) = t_alphanumeric0(input)?;
  let mut identifier = first.lexeme;
  for mut tk in rest {
    identifier.append(&mut tk.lexeme);
  }
  Ok((input,Node::Identifier{value: identifier}))
}

pub fn number(input: Tokens) -> IResult<Tokens, Node> {
  let (input, digits) = many1(t_digit)(input)?;
  let value: Vec<u8> = digits.iter()
                             .flat_map(|token| token.lexeme.iter())
                             .cloned()
                             .collect();
  Ok((input, Node::Number { value }))
}

pub fn boolean(input: Tokens) -> IResult<Tokens, Node> {
  let (input, token) = alt((t_true, t_false))(input)?;
   let value = match token.kind {
      TokenKind::True => true,
      TokenKind::False => false,
      _ => unreachable!(),
  };
  Ok((input, Node::Bool { value }))
}

pub fn string(input: Tokens) -> IResult<Tokens, Node> {
 let (input, _) = t_quote(input)?;
  let (input, string) = t_alphanumeric0(input)?;
 let (input, _) = t_quote(input)?;
 let value: Vec<u8> = string.into_iter()
                               .map(|token| token.lexeme)
                               .flatten()
                               .collect();
 Ok((input, Node::String{ value }))
}

pub fn function_call(input: Tokens) -> IResult<Tokens, Node> {
  let (input, fxn_name) = identifier(input)?;
  let (input, _) = (t_left_paren)(input)?;
  let (input, args) = many0(arguments)(input)?;
  let (input, _) = (t_right_paren)(input)?;
  let args = if args.is_empty() {
    vec![Node::FunctionArguments{ children: vec![]}]
  } else {
    args
  };
  let name: Vec<u8> = match fxn_name {
    Node::Identifier{value} => value,
    _ => unreachable!(),
  }; 
  Ok((input, Node::FunctionCall{name, children: args}))
}

pub fn value(input: Tokens) -> IResult<Tokens, Node> {
  alt((number, identifier, boolean))(input)
}

pub fn math_expression(input: Tokens) -> IResult<Tokens, Node> {
  let (input, leftside) = value(input)?;
  let (input, operator) = alt((t_plus, t_dash))(input)?;
  let (input, rightside) = value(input)?;
  let name = match operator.kind {
    TokenKind::Plus => b"add",
    TokenKind::Dash => b"sub",
    _ => unreachable!(),
  };
  Ok((input, Node::MathExpression{name: name.to_vec(), children: vec![leftside, rightside] }))
}


pub fn expression(input: Tokens) -> IResult<Tokens, Node> {
   let (input, result) =  alt((boolean, math_expression, function_call, number, string,identifier))(input)?;
   Ok((input, Node::Expression{children: vec! [result]}))
}

pub fn statement(input: Tokens) -> IResult<Tokens, Node> {
  let (input, result) = alt((variable_define, expression, function_return))(input)?;
  let (input, _) = (t_semicolon)(input)?;
  Ok((input, result))
}

pub fn function_return(input: Tokens) -> IResult<Tokens, Node> {
  let (input, _) = t_return(input)?;
  let (input, result) = alt((function_call,expression, identifier))(input)?;
  Ok((input, Node::FunctionReturn{children: vec! [result]}))
}

pub fn variable_define(input: Tokens) -> IResult<Tokens, Node> {
  let (input, _) = t_let(input)?;
  let (input, variable) = identifier(input)?;
  let (input, _) = (t_equal)(input)?;
  let (input, expression) = expression(input)?;
  Ok((input, Node::VariableDefine{children: vec![variable,expression]}))
}

pub fn arguments(input: Tokens) -> IResult<Tokens, Node> {
  let (input, arg) = expression(input)?;
  let (input, mut others) = many0(other_arg) (input)?;
  let mut args = vec! [arg];
  args.append (&mut others) ;
  Ok((input, Node::FunctionArguments{children: args}))
}


pub fn other_arg(input: Tokens) -> IResult<Tokens, Node> {
  let (input, _) = t_comma(input)?;
  expression(input)
}

pub fn function_define(input: Tokens) -> IResult<Tokens, Node> {
  let (input, _) = t_fn(input)?;
  let (input, fxn_name) = identifier(input)?;
  let name = match fxn_name {
    Node::Identifier{value} => value,
    _ => unreachable!(),
  };
  let (input, _) = t_left_paren(input)?;
  let (input, args) = many0(arguments)(input)?;
  let (input, _) = t_right_paren(input)?;
  let (input, _) = t_left_curly(input)?;
  let (input, statements) = many1(statement)(input)?;
  let (input, _) = t_right_curly(input)?;
  let fxn_statements = Node::FunctionStatements{children: statements};
  let fxn_arguments = if args.is_empty() {
    Node::FunctionArguments{children: vec![]}
  } else {
    args[0].clone()
  };
  Ok((input, Node::FunctionDefine{name, children: vec![fxn_arguments,fxn_statements] }))
}

pub fn comment(input: Tokens) -> IResult<Tokens, Node> {
  let mut comment_text = Vec::new();
  let (input, _) = t_slash(input)?;
  let (input, _) = t_slash(input)?;
  let (input, alpha_tokens) = many0(t_alpha)(input)?;
  for token in alpha_tokens {
    comment_text.extend_from_slice(&token.lexeme);
  }
  Ok((input, Node::Comment{ value: comment_text }))
}

pub fn program(input: Tokens) -> IResult<Tokens, Node> {
  let (input, result) = many1(function_define)(input)?;
  Ok((input, Node::Program{ children: result }))
}

