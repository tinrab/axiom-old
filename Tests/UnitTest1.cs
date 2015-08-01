using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom;

namespace Tests
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
            var engine = new Engine();
            var program = engine.Compile("x = 42;");
        }
    }
}
