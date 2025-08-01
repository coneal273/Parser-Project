var describe: any;
var it: any;
var expect: any;

describe("Tests of web front end", function() {

    /**
     * Unit test for adding one to a variable
     */
    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    /**
     * Unit test for subtracting one from a variable
     */
    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });

    /**
     * I wasn't able to figure out how to make unit tests when using react typescript
     * From the tutorials I tried doing a similar interaction with html elements
     * but because of the way react renders components and uses routes I was confused on
     * how to get unit tests for UI working. Soon we will try to find a better testing library
     * that is specifically for react use since I think the library we used for this is easier
     * to use with vanilla html css js
     */
    
    // it("UI Test: Add Button Hides Listing", function(){
    //     // click the button for showing the add button
    //     (<HTMLElement>document.getElementById("showFormButton")).click();
    //     // expect that the add form is not hidden
    //     expect( (<HTMLElement>document.getElementById("addElement")).style.display ).toEqual("block");
    //     // expect that the element listing is hidden
    //     expect( (<HTMLElement>document.getElementById("showElements")).style.display ).toEqual("none");
    //     // reset the UI, so we don't mess up the next test
    //     (<HTMLElement>document.getElementById("addCancel")).click();        
    // });
});