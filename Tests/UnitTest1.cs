using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom;
using System.IO;

namespace Tests
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
            var engine = new Engine();

            try {
                var program = engine.Compile(new StreamReader("test.ax"));
            } catch (Exception e) {
                Console.WriteLine(e.Message);
            }
        }
    }
}
