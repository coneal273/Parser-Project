use midterm2::*;
use midterm2::Node::*;

macro_rules! test {
  ($func:ident, $input:tt, $combinator:tt, $test:expr) => (
    #[test]
    fn $func() -> Result<(),()> {
      let source = $input;
      let tokens = lex(source);
      let parse_result = $combinator(tokens);
      match parse_result {
        Ok((_,tree)) => {
          assert_eq!(tree,$test)
        },
        _ => {assert!(false)},
      }
      Ok(())
    }
  )
}
// test name, test string, combinator,  expected result
test!(test_ident, r#"hello"#, identifier, Identifier{value: vec![104, 101, 108, 108, 111]});
test!(test_number, r#"123"#, number, Number{value: vec![49, 50, 51]});
test!(test_bool, r#"true"#, boolean, Bool{value: true});
test!(test_string, r#""hello""#, string, String{value: vec![104, 101, 108, 108, 111]});
test!(test_function_call, r#"foo()"#, function_call, FunctionCall{name: vec![102, 111, 111], children: vec![
  FunctionArguments{ children: vec![
  ]}
]});
test!(test_function_call_one_arg, r#"foo(a)"#, function_call, FunctionCall{name: vec![102, 111, 111], children: vec![
  FunctionArguments{ children: vec![
    Expression { children: vec![Identifier { value: vec![97] }]}
  ]}
]});
test!(test_variable_define_number, r#"let a = 123;"#, variable_define, VariableDefine{children: vec![
  Identifier { value: vec![97] },
  Expression { children: vec![Number{value: vec![49, 50, 51] }]}
]});
test!(test_variable_define_bool, r#"let a = true;"#, variable_define, VariableDefine{children: vec![
  Identifier { value: vec![97] },
  Expression { children: vec![Bool{value: true}]}
]});
test!(test_math_expr, r#"1+1;"#, math_expression, MathExpression {name: vec![97, 100, 100], children: vec![
  Number{value: vec![49]},
  Number{value: vec![49]}
]});
test!(test_variable_define_math_expr, r#"let a = 1 + 1;"#, variable_define, VariableDefine{children: vec![
  Identifier { value: vec![97] },
  Expression { children: vec![
    MathExpression {name: vec![97, 100, 100], children: vec![
      Number{value: vec![49]},
      Number{value: vec![49]}
    ]}
  ]}
]});
test!(test_variable_function_call, r#"let a = foo();"#, variable_define, VariableDefine{children: vec![
  Identifier { value: vec![97] },
  Expression { children: vec![
    FunctionCall{name: vec![102, 111, 111], children: vec![
      FunctionArguments{ children: vec![
      ]}
    ]}
  ]}
]});
test!(test_function_define, r#"fn a(){return 1;}"#, function_define, FunctionDefine{
  name: vec![97],
  children: vec![
    FunctionArguments{ children: vec![] },
    FunctionStatements{ children: vec![
      FunctionReturn{ children: vec![ 
        Expression { children: vec![Number{value: vec![49] }]}
      ]}
    ]}
  ]
});
test!(test_function_define_multi_statements, r#"fn add(a,b){let x=a+b;return x;}"#, function_define, FunctionDefine{
  name: vec![97, 100, 100],
  children: vec![
    FunctionArguments{ children: vec![
      Expression { children: vec![Identifier { value: vec![97] }] },
      Expression { children: vec![Identifier { value: vec![98] }] },
    ] },
    FunctionStatements{ children: vec![
      VariableDefine{children: vec![
        Identifier { value: vec![120] },
        Expression { children: vec![
          MathExpression {name: vec![97, 100, 100], children: vec![
            Identifier{value: vec![97]},
            Identifier{value: vec![98]}
          ]}
        ]}
      ]},
      FunctionReturn{ children: vec![ 
        Expression { children: vec![Identifier{value: vec![120] }]}
      ]}
    ]}
  ]
});


test!(test_l1, r#"l1 = l2, <l1_infix> ;"# , l2, Number{value: vec![50] });
test!(test_l1_addition, r#"2 + 3"#, l1, MathExpression {
  name:vec![43],
  children: vec![
    Number{value: vec![50]},
    Number{value: vec![51]}
  ]
});


test!(test_l1_inf_sub, r#"2 - 3"#, l1, MathExpression {
  name:vec![45],
  children: vec![
    Number{value: vec![50]},
    Number{value: vec![51]},
  ]
});

test!(test_l2_single, r#"2"#, l2, Number{value: vec![50] });
test!(test_l2_multiplication, r#"2 * 3"#, l2, MathExpression {
  name:vec![42],
  children: vec![
    Number{value: vec![50]},
    Number{value: vec![51]}
  ]
});

test!(test_loop_statement, r#"loop_statement { 2 }"#, loop_statement, LoopStatement {
  children: vec![
    Expression { children: vec![
      Number { value: vec![50] }
    ]}
  ]
});


